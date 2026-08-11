"""
Seeds a small but realistic 'enterprise' demo database (SQLite) so the agent
is runnable out of the box. Swap DATABASE_URL in .env to point at a real
Postgres/Snowflake/SQL Server warehouse for production use -- no other code
changes are required since access goes through SQLAlchemy.
"""
import sqlite3
import random
from datetime import date, timedelta
from pathlib import Path

DB_PATH = Path(__file__).parent / "enterprise_demo.db"

SCHEMA = """
CREATE TABLE IF NOT EXISTS regions (
    region_id INTEGER PRIMARY KEY,
    region_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id INTEGER PRIMARY KEY,
    product_name TEXT NOT NULL,
    category TEXT NOT NULL,
    unit_cost REAL NOT NULL,
    unit_price REAL NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
    customer_id INTEGER PRIMARY KEY,
    customer_name TEXT NOT NULL,
    segment TEXT NOT NULL,           -- Enterprise / SMB / Public Sector
    region_id INTEGER NOT NULL,
    FOREIGN KEY (region_id) REFERENCES regions(region_id)
);

CREATE TABLE IF NOT EXISTS sales (
    sale_id INTEGER PRIMARY KEY,
    sale_date TEXT NOT NULL,
    customer_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    revenue REAL NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id INTEGER PRIMARY KEY,
    employee_name TEXT NOT NULL,
    department TEXT NOT NULL,
    region_id INTEGER NOT NULL,
    hire_date TEXT NOT NULL,
    FOREIGN KEY (region_id) REFERENCES regions(region_id)
);
"""

def seed():
    DB_PATH.parent.mkdir(exist_ok=True)
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()
    cur.executescript(SCHEMA)

    regions = ["North America", "EMEA", "APAC", "LATAM"]
    cur.executemany("INSERT OR IGNORE INTO regions VALUES (?,?)",
                     [(i + 1, r) for i, r in enumerate(regions)])

    categories = ["Software", "Hardware", "Services", "Support"]
    products = [
        (1, "Cloud Analytics Suite", "Software", 120, 499),
        (2, "Enterprise CRM", "Software", 200, 899),
        (3, "Edge Gateway Device", "Hardware", 300, 750),
        (4, "Managed Onboarding", "Services", 150, 400),
        (5, "Premium Support Plan", "Support", 50, 199),
        (6, "Data Warehouse Connector", "Software", 90, 350),
        (7, "AI Insights Add-on", "Software", 60, 299),
    ]
    cur.executemany("INSERT OR IGNORE INTO products VALUES (?,?,?,?,?)", products)

    segments = ["Enterprise", "SMB", "Public Sector"]
    customers = []
    for i in range(1, 41):
        customers.append((
            i, f"Customer {i:02d}", random.choice(segments), random.randint(1, 4)
        ))
    cur.executemany("INSERT OR IGNORE INTO customers VALUES (?,?,?,?)", customers)

    departments = ["Sales", "Customer Success", "Engineering", "Finance"]
    employees = []
    for i in range(1, 21):
        hire = date(2020, 1, 1) + timedelta(days=random.randint(0, 1800))
        employees.append((
            i, f"Employee {i:02d}", random.choice(departments),
            random.randint(1, 4), hire.isoformat()
        ))
    cur.executemany("INSERT OR IGNORE INTO employees VALUES (?,?,?,?,?)", employees)

    cur.execute("SELECT COUNT(*) FROM sales")
    if cur.fetchone()[0] == 0:
        start = date(2024, 1, 1)
        sales = []
        sale_id = 1
        for day_offset in range(0, 540):  # ~18 months of activity
            d = start + timedelta(days=day_offset)
            for _ in range(random.randint(2, 6)):
                cust = random.randint(1, 40)
                prod_id, _, _, cost, price = random.choice(products)
                qty = random.randint(1, 15)
                revenue = round(qty * price * random.uniform(0.9, 1.1), 2)
                sales.append((sale_id, d.isoformat(), cust, prod_id, qty, revenue))
                sale_id += 1
        cur.executemany("INSERT INTO sales VALUES (?,?,?,?,?,?)", sales)

    conn.commit()
    conn.close()
    print(f"Seeded demo database at {DB_PATH}")

if __name__ == "__main__":
    seed()

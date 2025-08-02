// Create user and database for the application
db = db.getSiblingDB('franchise_system');

db.createUser({
  user: 'franchise_user',
  pwd: 'franchise_pass',
  roles: [
    {
      role: 'readWrite',
      db: 'franchise_system'
    }
  ]
});

// Create indexes to optimize queries
db.franchises.createIndex({ "name.value": 1 });
db.franchises.createIndex({ "branches.id": 1 });
db.franchises.createIndex({ "branches.products.id": 1 });

print('Database initialized successfully!');

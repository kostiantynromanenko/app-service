db.createUser(
    {
      user: "mongo",
      pwd: "mongo123",
      roles: [
        {
          role: "readWrite",
          db: "games"
        }
      ]
    }
);
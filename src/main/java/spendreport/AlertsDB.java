package spendreport;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;


public class AlertsDB
{
    private MongoClient dbClient;
    private MongoCredential credential;
    private MongoDatabase db;
    private MongoCollection<Document> alertsCollection;

    public AlertsDB()
    {
        this.dbClient = new MongoClient();

        // create Mongo credentials
		this.credential = MongoCredential.createCredential("mongoUser", "alertsDB", "password".toCharArray());
		System.out.println("Connected to DB successfully");

        // access DB
		this.db = this.dbClient.getDatabase("alertsDB");
		System.out.println("Credentials ::" + this.credential);

        try {
			// create collection with alerts if not exists
			this.db.createCollection("alerts");
			System.out.println("Collection 'alerts' created successfully");
		}
		catch(Exception e) {
			System.out.println("Collection 'alerts' already exists");
		}

        // retrieve collection with alerts
		this.alertsCollection = this.db.getCollection("alerts");
		System.out.println("Collection 'alerts' selected successfully");
    }

    public void insertAlert(Alert alert)
    {
		Document doc = new Document("honeypotId", alert.getHoneypotId())
			.append("timestamp", alert.getTimestamp())
			.append("attacker", alert.getAttacker())
			.append("command", alert.getCommand())
			.append("level", alert.getLevelValue());

		// insert alert into collection with alerts
		this.alertsCollection.insertOne(doc);
		System.out.println("Alert inserted into DB successfully");
    }
}

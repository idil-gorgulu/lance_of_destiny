package org.Controllers;

import org.Utils.Database;
import org.Views.Navigator;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javax.swing.JOptionPane;

public class SignupPageController {
    private static SignupPageController instance;

    public static SignupPageController getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new SignupPageController();
            return instance;
        }
    }

    public void createUser(String email, String username, String password) {
        // Parola geçerlilik kontrolü
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters long and contain at least one digit.");
            return;
        }

        // Kullanıcı adı ve e-posta adresi benzersizlik kontrolü
        if (emailExists(email)) {
            JOptionPane.showMessageDialog(null, "Email already exists.");
            return;
        }

        if (usernameExists(username)) {
            JOptionPane.showMessageDialog(null, "Username already exists.");
            return;
        }

        // Kullanıcı kaydı
        Document user = new Document();
        user.put("email", email);
        user.put("username", username);
        user.put("password", password); // Güvenlik için parolayı hash'lemek önerilir
        Database.getInstance().getUserCollection().insertOne(user);
        Navigator.getInstance().showEnterPage(); // Kullanıcı başarıyla oluşturulduktan sonra ana sayfaya yönlendir
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*\\d.*");
    }

    private boolean emailExists(String email) {
        MongoCollection<Document> collection = Database.getInstance().getUserCollection();
        return collection.countDocuments(Filters.eq("email", email)) > 0;
    }

    private boolean usernameExists(String username) {
        MongoCollection<Document> collection = Database.getInstance().getUserCollection();
        return collection.countDocuments(Filters.eq("username", username)) > 0;
    }
}

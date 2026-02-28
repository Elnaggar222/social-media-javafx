package controller;

import dao.FriendDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.User;

import java.util.List;

public class FriendsController {

    @FXML
    private ListView<User> friendsListView;

    private final FriendDAO friendDAO = new FriendDAO();

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadFriends();
    }

    private void loadFriends() {

        List<User> friends =
                friendDAO.getFriends(currentUser.getId());

        friendsListView.getItems().setAll(friends);

        friendsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }
}
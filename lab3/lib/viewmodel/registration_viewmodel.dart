import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/cupertino.dart';

class RegistrationViewModel extends ChangeNotifier {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final DatabaseReference _database = FirebaseDatabase.instance.ref();

  bool isShowFilmsListView = false;
  bool isShowAlert = false;

  Future<void> registerUser(String login, String email, String password) async {
    if (login.length < 4 || password.length < 8 || email.isEmpty) {
      return;
    }

    final UserCredential result = await _auth.createUserWithEmailAndPassword(
      email: email,
      password: password,
    );

    final user = result.user;
    if (user == null) {
      return;
    }

    final userData = {
      'login': login,
      'email': email,
    };

    await _database.child('users').child(user.uid).set(userData);
    isShowFilmsListView = true;
    notifyListeners();
  }
}

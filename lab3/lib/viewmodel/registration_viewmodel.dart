import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:rxdart/rxdart.dart';

class RegistrationViewModel {
  final BehaviorSubject<String> login = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<String> email = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<String> password = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<String> alert = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<bool> isShowFilmsListView = BehaviorSubject<bool>.seeded(false);
  final BehaviorSubject<bool> isShowAlert = BehaviorSubject<bool>.seeded(false);

  final FirebaseAuth auth = FirebaseAuth.instance;
  final FirebaseDatabase database = FirebaseDatabase.instance;

  void hidePopup() {
    isShowAlert.add(false);
  }

  void showAlert(String message) {
    alert.add(message);
    isShowAlert.add(true);
  }

  void registerUser() async {
    if (login.value.length < 4 || password.value.length < 8 || email.value.isEmpty) {
      showAlert("Invalid data in input fields");
      return;
    }

    try {
      UserCredential userCredential = await auth.createUserWithEmailAndPassword(
        email: email.value,
        password: password.value,
      );

      User? user = userCredential.user;

      if (user == null) {
        showAlert("No user created");
        return;
      }

      Map<String, dynamic> userData = {
        "login": login.value,
        "email": email.value,
      };

      await database.ref("users").child(user.uid).set(userData);

      isShowFilmsListView.add(true);
    } on FirebaseAuthException catch (e) {
      showAlert("Error creating user: ${e.message}");
    } catch (e) {
      showAlert("Error creating user: $e");
    }
  }

  void dispose() {
    login.close();
    email.close();
    password.close();
    alert.close();
    isShowFilmsListView.close();
    isShowAlert.close();
  }
}
import 'package:firebase_auth/firebase_auth.dart';
import 'package:rxdart/rxdart.dart';

class AuthorizationViewModel {
  final BehaviorSubject<String> email = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<String> password = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<String> alert = BehaviorSubject<String>.seeded('');
  final BehaviorSubject<bool> isShowFilmsListView =
      BehaviorSubject<bool>.seeded(false);
  final BehaviorSubject<bool> isShowAlert = BehaviorSubject<bool>.seeded(false);

  final FirebaseAuth auth = FirebaseAuth.instance;

  void hidePopup() {
    isShowAlert.add(false);
  }

  void showAlert(String message) {
    alert.add(message);
    isShowAlert.add(true);
  }

  void authorization() async {
    if (password.value.length < 8 || email.value.isEmpty) {
      showAlert("Invalid data in input fields");
      return;
    }

    try {
      await auth.signInWithEmailAndPassword(
          email: email.value, password: password.value);
      isShowFilmsListView.add(true);
    } on FirebaseAuthException catch (e) {
      showAlert("Error signing in: ${e.message}");
    } catch (e) {
      showAlert("Error signing in: $e");
    }
  }

  void dispose() {
    email.close();
    password.close();
    alert.close();
    isShowFilmsListView.close();
    isShowAlert.close();
  }
}

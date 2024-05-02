import 'package:firebase_auth/firebase_auth.dart';

class AuthorizationViewModel {
  final FirebaseAuth _auth = FirebaseAuth.instance;

  bool isShowFilmsListView = false;
  bool isShowAlert = false;

  Future<void> authorization(String email, String password) async {
    if (password.length < 8 || email.isEmpty) {
      return;
    }

    await _auth.signInWithEmailAndPassword(
      email: email,
      password: password,
    );
    isShowFilmsListView = true;
  }
}

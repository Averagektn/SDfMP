import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/cupertino.dart';

import '../model/user.dart' as my;

class ProfileViewModel extends ChangeNotifier {
  final my.User user;
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseDatabase _database = FirebaseDatabase.instance;

  ProfileViewModel(this.user);

  Future<void> loadData() async {
    final uid = _auth.currentUser?.uid;
    if (uid == null) return;

    final snapshot = await _database.ref().child("users").child(uid).get();
    if (snapshot.exists) {
      final data = snapshot.value as Map<dynamic, dynamic>;
      user.information = data["info"] ?? "";
      user.username = data["name"] ?? "";
      user.surname = data["surname"] ?? "";
      user.patronymic = data["patronymic"] ?? "";
      user.gender = data["gender"] ?? "";
      user.country = data["country"] ?? "";
      user.favoredFilm = data["favoredFilm"] ?? "";
      user.favoredGenre = data["favoredGenre"] ?? "";
      notifyListeners();
    }
  }

  Future<void> update() async {
    final uid = _auth.currentUser?.uid;
    if (uid == null) return;

    final ref = _database.reference().child("users").child(uid);
    final userMap = {
      "login": user.login,
      "email": user.email,
      "info": user.information,
      "name": user.username,
      "surname": user.surname,
      "patronymic": user.patronymic,
      "gender": user.gender,
      "country": user.country,
      "favoredFilm": user.favoredFilm,
      "favoredGenre": user.favoredGenre,
    };
    await ref.set(userMap);
    notifyListeners();
  }

  Future<void> deleteProfile() async {
    final uid = _auth.currentUser?.uid;
    if (uid == null) return;

    await _database.ref().child("users").child(uid).remove();
    await _database.ref().child("favored").child(uid).remove();

    await _auth.currentUser!.delete();
  }

  Future<void> logout() async {
    await _auth.signOut();
  }
}
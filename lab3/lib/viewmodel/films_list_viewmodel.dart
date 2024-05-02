import 'package:flutter/material.dart';
import 'package:firebase_database/firebase_database.dart';

import '../model/film.dart';

class FilmsListViewModel extends ChangeNotifier {
  List<Film> films = [];

  final database = FirebaseDatabase.instance;

  void loadFilms() {
    final filmsRef = database.ref().child('films');
    filmsRef.onValue.listen((event) {
      final snapshot = event.snapshot;
      final films = <Film>[];
      for (final child in snapshot.children) {
        final filmId = child.key;
        final filmValue = child.value as Map<dynamic, dynamic>;
        final film = Film(
            name: filmValue['name'] as String? ?? '',
            categories:
                List<String>.from(filmValue['categories'] as List? ?? []),
            description: filmValue['description'] as String? ?? '',
            id: filmId);
        films.add(film);
      }
      this.films = films;
      notifyListeners();
    });
  }
}

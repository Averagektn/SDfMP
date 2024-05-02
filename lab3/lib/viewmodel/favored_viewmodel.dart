import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';

import '../model/film.dart';

class FavoredViewModel extends ChangeNotifier {
  final FirebaseDatabase _database = FirebaseDatabase.instance;
  final FirebaseAuth _auth = FirebaseAuth.instance;

  List<Film> _films = [];

  List<Film> get films => _films;

  void loadFilms() {
    final uid = _auth.currentUser?.uid;
    if (uid == null) {
      return;
    }

    final favoredRef = _database.ref('favored').child(uid);
    favoredRef.onValue.listen((event) {
      final favoredFilmIds = <String>[];
      for (final child in event.snapshot.children) {
        final filmId = child.value as String?;
        if (filmId != null) {
          favoredFilmIds.add(filmId);
        }
      }

      final filmsRef = _database.ref('films');
      filmsRef.onValue.listen((event) {
        final films = <Film>[];
        for (final child in event.snapshot.children) {
          final filmId = child.key;
          final filmValue = child.value as Map<dynamic, dynamic>;
          if (filmId != null && favoredFilmIds.contains(filmId)) {
            final film = Film(
                name: filmValue['name'] as String? ?? '',
                categories:
                    List<String>.from(filmValue['categories'] as List? ?? []),
                description: filmValue['description'] as String? ?? '',
                id: filmId);
            films.add(film);
          }
        }
        _films = films;
        notifyListeners();
      });
    });
  }
}

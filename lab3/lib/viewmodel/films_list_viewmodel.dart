import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:rxdart/rxdart.dart';

import '../model/film.dart';

class FilmsListViewModel {
  final BehaviorSubject<List<Film>> films = BehaviorSubject<List<Film>>.seeded([]);

  final FirebaseFirestore database = FirebaseFirestore.instance;

  FilmsListViewModel() {
    loadFilms();
  }

  void loadFilms() {
    final filmsRef = database.collection('films');

    filmsRef.snapshots().listen((snapshot) {
      snapshot.docs.map((doc) {
        final data = doc.data();
        films.value.add(Film(
          id: doc.id,
          name: data['name'] as String,
          categories: (data['categories'] as List).cast<String>(),
          description: data['description'] as String,
        ));
      });
    });
  }

  void dispose() {
    films.close();
  }
}
import 'dart:typed_data';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart';

import '../model/comment.dart';
import '../model/film.dart';

class FilmViewModel extends ChangeNotifier {
  static final cache = <String, ImageProvider>{};
  static final cachedSlider = <String, List<ImageProvider>>{};

  final Film film;
  final _uid = FirebaseAuth.instance.currentUser!.uid;
  final _storage = FirebaseStorage.instance;
  final _database = FirebaseDatabase.instance;

  List<ImageProvider> images = [];
  List<Comment> comments = [];
  String newComment = "";

  FilmViewModel(this.film);

  Future<void> removeFromFavored() async {
    final favoredRef = _database.ref().child("favored").child(_uid);
    final snapshot = await favoredRef.get();
    if (snapshot.exists) {
      final data = Map<String, dynamic>.from(snapshot.value as Map);
      data.removeWhere((_, value) => value == film.id);
      await favoredRef.set(data);
    }
  }

  Future<bool> checkIfFilmIdExists() async {
    final favoredRef = _database.ref().child("favored").child(_uid);
    final snapshot = await favoredRef.get();
    if (snapshot.exists) {
      final data = Map<String, dynamic>.from(snapshot.value as Map);
      return data.containsValue(film.id);
    }
    return false;
  }

  Future<void> addToFavored() async {
    final favoredRef = _database.ref().child("favored").child(_uid);
    await favoredRef.push().set(film.id);
  }

  Future<void> addComment() async {
    final loginSnapshot = await _database.ref().child("users").child(_uid).child("login").get();
    final author = loginSnapshot.value as String;
    final comment = Comment(author: author, text: newComment);
    comments.add(comment);
    notifyListeners();

    final commentsRef = _database.ref().child("comments").child(film.id!);
    await commentsRef.push().set({
      "author": comment.author,
      "text": comment.text,
    });
  }

  Future<void> getSlider() async {
    final cacheKey = film.id!;
    if (cachedSlider.containsKey(cacheKey)){
      images = cachedSlider[cacheKey]!;
    } else {
      final sliderRef = _storage.ref().child("film_images").child(film.id!);

      final result = await sliderRef.listAll();
      for (final file in result.items) {
        final url = await file.getDownloadURL();

        final response = await get(Uri.parse(url));
        final bytes = response.bodyBytes;

        final imageData = Uint8List.fromList(bytes);

        images.add(MemoryImage(imageData));
        notifyListeners();
      }

      cachedSlider[cacheKey] = images;
    }
    notifyListeners();
  }

  Future<void> getComments() async {
    final commentsRef = _database.ref().child("comments").child(film.id!);
    final snapshot = await commentsRef.get();
    if (snapshot.exists) {
      final commentsData = Map<String, dynamic>.from(snapshot.value as Map);
      comments = commentsData.values.map((commentData) {
        final commentDict = Map<String, String>.from(commentData as Map);
        return Comment(author: commentDict["author"]!, text: commentDict["text"]!);
      }).toList();
      notifyListeners();
    }
  }

  Future<ImageProvider?> getImage() async {
    if (cache.containsKey(film.id)) {
      return cache[film.id];
    } else {
      return await loadImage();
    }
  }

  Future<ImageProvider?> loadImage() async {
    if (cache.containsKey(film.id)) {
      return cache[film.id];
    }

    final posterRef = _storage.ref().child("posters").child(film.id!);
    final url = await posterRef.getDownloadURL();

    final response = await get(Uri.parse(url));
    final bytes = response.bodyBytes;

    final imageData = Uint8List.fromList(bytes);

    cache[film.id!] = MemoryImage(imageData);
    return cache[film.id!];
  }
}
import 'dart:async';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/cupertino.dart';

import 'package:http/http.dart' as http;

import '../model/comment.dart';
import '../model/film.dart';

class FilmViewModel extends ChangeNotifier {
  // Use a Map instead of NSCache for caching
  static final Map<String, Image> _cache = {};
  static final Map<String, List<Image>> _cachedSlider = {};

  final Film film;

  final String? uid = FirebaseAuth.instance.currentUser?.uid;
  final storage = FirebaseStorage.instance;
  final database = FirebaseDatabase.instance;

  List<Image> images = [];
  List<Comment> comments = [];
  String newComment = "";

  FilmViewModel(this.film);

  Future<void> removeFromFavored() async {
    final favoredRef = database.ref().child("favored").child(uid!);

    final snapshot = await favoredRef.get();
    if (snapshot.exists) {
      final data = Map<String, dynamic>.from(snapshot.value as Map);
      data.removeWhere((key, value) => value == film.id);
      await favoredRef.set(data);
    }
  }

  Future<bool> checkIfFilmIdExists() async {
    final favoredRef = database.ref().child("favored").child(uid!);

    final snapshot = await favoredRef.get();
    if (snapshot.exists) {
      final data = Map<String, dynamic>.from(snapshot.value as Map);
      return data.containsValue(film.id);
    }
    return false;
  }

  Future<void> addToFavored() async {
    final favoredRef = database.ref().child("favored").child(uid!);
    await favoredRef.push().set(film.id);
  }

  Future<void> addComment() async {
    final snapshot = await database.ref().child("users").child(uid!).child("login").get();
    final comment = Comment(author: snapshot.value as String, text: newComment);
    final commentsRef = database.ref().child("comments").child(film.id!);
    final commentDictionary = {
      "author": comment.author,
      "text": comment.text,
    };
    await commentsRef.push().set(commentDictionary);
    comments.add(comment);
    notifyListeners();
  }

  Future<void> getSlider() async {
    final sliderRef = storage.ref().child("film_images").child(film.id!);

    final cacheKey = film.id!;

    if (_cachedSlider.containsKey(cacheKey)) {
      images = _cachedSlider[cacheKey]!;
      notifyListeners();
      return;
    }

    final result = await sliderRef.listAll();
    images = [];

    for (final file in result.items) {
      final data = await file.getData();
      if (data != null) {
        final image = Image.memory(data);
        images.add(image);
      }
    }

    _cachedSlider[cacheKey] = images;
    notifyListeners();
  }

  Future<void> getComments() async {
    final commentsRef = database.ref().child("comments").child(film.id!);

    final snapshot = await commentsRef.get();
    if (snapshot.exists) {
      final commentsData = Map<String, dynamic>.from(snapshot.value as Map);
      comments = commentsData.values.map((commentData) {
        final commentDict = Map<String, String>.from(commentData);
        return Comment(author: commentDict["author"]!, text: commentDict["text"]!);
      }).toList();
      notifyListeners();
    }
  }

  Future<Image?> getImage() async {
    if (_cache.containsKey(film.id!)) {
      return _cache[film.id!];
    } else {
      // Use a Completer instead of DispatchQueue and usleep
      final completer = Completer<Image?>();
      while (!_cache.containsKey(film.id!)) {
        await Future.delayed(const Duration(milliseconds: 10));
      }
      completer.complete(_cache[film.id!]);
      return completer.future;
    }
  }

  Future<void> loadImage() async {
    if (!_cache.containsKey(film.id!)) {
      final posterRef = storage.ref().child("posters").child(film.id!);
      final url = await posterRef.getDownloadURL();
      final response = await http.get(url as Uri);
      if (response.statusCode == 200) {
        final image = Image.memory(response.bodyBytes);
        _cache[film.id!] = image;
      }
        }
  }
}
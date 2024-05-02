import 'package:flutter/material.dart';
import 'package:lab3/view/favored_view.dart';
import 'package:lab3/view/films_list_view.dart';
import 'package:lab3/view/profile_view.dart';

import '../model/film.dart';
import '../viewmodel/film_viewmodel.dart';
import 'comment_view.dart';

class FilmView extends StatefulWidget {
  final Film film;

  const FilmView({super.key, required this.film});

  @override
  State<FilmView> createState() => _FilmViewState();
}

class _FilmViewState extends State<FilmView> {
  late FilmViewModel _viewModel;
  bool _isAddedToFavored = false;

  @override
  void initState() {
    super.initState();
    _viewModel = FilmViewModel(widget.film);
    _viewModel.addListener(() {
      setState(() {});
    });
    _viewModel.getComments();
    _viewModel.getSlider();
    _viewModel.checkIfFilmIdExists().then((isInFavored) {
      setState(() {
        _isAddedToFavored = isInFavored;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.film.name),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(widget.film.name,
                        style: const TextStyle(
                            fontSize: 24, fontWeight: FontWeight.bold)),
                  ),
                  IconButton(
                    icon: Icon(
                        _isAddedToFavored ? Icons.star : Icons.star_border),
                    onPressed: () {
                      if (_isAddedToFavored) {
                        _viewModel.removeFromFavored();
                      } else {
                        _viewModel.addToFavored();
                      }
                      setState(() {
                        _isAddedToFavored = !_isAddedToFavored;
                      });
                    },
                  ),
                ],
              ),
              SizedBox(
                height: 200,
                child: _viewModel.images.isNotEmpty
                    ? PageView.builder(
                        itemCount: _viewModel.images.length,
                        itemBuilder: (context, index) {
                          return Image(image: _viewModel.images[index]);
                        },
                      )
                    : const CircularProgressIndicator(),
              ),
              Text(widget.film.categories.join(', '),
                  style: const TextStyle(fontSize: 18)),
              Text(widget.film.description),
              ListView.builder(
                shrinkWrap: true,
                itemCount: _viewModel.comments.length,
                itemBuilder: (context, index) {
                  final comment = _viewModel.comments[index];
                  return CommentView(comment: comment);
                },
              ),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      decoration:
                          const InputDecoration(hintText: 'Enter comment'),
                      onChanged: (text) {
                        setState(() {
                          _viewModel.newComment = text;
                        });
                      },
                    ),
                  ),
                  ElevatedButton(
                    onPressed: _viewModel.addComment,
                    child: const Text('Send'),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const FilmsListView(),
                        ),
                      );
                    }, // Navigate to FilmsListView
                    child: const Text('Films'),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const ProfileView(),
                        ),
                      );
                    },
                    child: const Text('Profile'),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const FavoredView(),
                        ),
                      );
                    },
                    child: const Text('Favored'),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

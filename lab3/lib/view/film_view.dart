import 'package:flutter/material.dart';

import '../model/film.dart';
import '../viewmodel/film_viewmodel.dart';
import 'films_list_view.dart';

class FilmView extends StatefulWidget {
  final Film film;

  const FilmView({super.key, required this.film});

  @override
  State<FilmView> createState() => _FilmViewState();
}

class _FilmViewState extends State<FilmView> {
  late final FilmViewModel viewModel;
  bool isAddedToFavored = false;

  @override
  void initState() {
    super.initState();
    viewModel = FilmViewModel(widget.film);
    viewModel.getComments();
    viewModel.getSlider();
    viewModel.checkIfFilmIdExists().then((isInFavored) {
      setState(() {
        isAddedToFavored = isInFavored;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(viewModel.film.name),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  viewModel.film.name,
                  style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                ),
                IconButton(
                  onPressed: isAddedToFavored
                      ? () {
                    viewModel.removeFromFavored();
                    setState(() {
                      isAddedToFavored = false;
                    });
                  }
                      : null,
                  icon: const Icon(Icons.favorite, color: Colors.red),
                ),
                IconButton(
                  onPressed: !isAddedToFavored
                      ? () {
                    viewModel.addToFavored();
                    setState(() {
                      isAddedToFavored = true;
                    });
                  }
                      : null,
                  icon: const Icon(Icons.favorite_border),
                ),
              ],
            ),
            const SizedBox(height: 16),
            SizedBox(
              height: 200,
              child: PageView.builder(
                itemCount: viewModel.images.length,
                itemBuilder: (context, index) {
                  return Image(image: viewModel.images[index]);
                },
              ),
            ),
            const SizedBox(height: 16),
            Text(
              viewModel.film.categories.join(", "),
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(viewModel.film.description),
/*            const SizedBox(height: 16),
            ...viewModel.comments.map((comment) => CommentView(comment: comment)),*/
            const SizedBox(height: 16),
            TextField(
              decoration: const InputDecoration(hintText: "Enter comment"),
              onChanged: (value) => viewModel.newComment = value,
            ),
            const SizedBox(height: 8),
            ElevatedButton(
              onPressed: viewModel.addComment,
              child: const Text("Send"),
            ),
            const SizedBox(height: 24),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => FilmsListView(),
                      ),
                    );
                  },
                  child: const Text("Films"),
                ),
/*                ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => ProfileView(),
                      ),
                    );
                  },
                  child: const Text("Profile"),
                ),*/
/*                ElevatedButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => FavoredView(),
                      ),
                    );
                  },
                  child: const Text("Favored"),
                ),*/
              ],
            ),
          ],
        ),
      ),
    );
  }
}
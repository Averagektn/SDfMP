import 'package:flutter/material.dart';

import '../viewmodel/film_viewmodel.dart';
import '../viewmodel/films_list_viewmodel.dart';
import 'film_row.dart';

class FilmsListView extends StatefulWidget {
  const FilmsListView({super.key});

  @override
  State<FilmsListView> createState() => _FilmsListViewState();
}

class _FilmsListViewState extends State<FilmsListView> {
  final FilmsListViewModel viewModel = FilmsListViewModel();
  String searchText = "";

  @override
  void initState() {
    super.initState();
    viewModel.loadFilms();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Films"),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              decoration: const InputDecoration(
                hintText: "Search",
                border: OutlineInputBorder(),
              ),
              onChanged: (value) {
                setState(() {
                  searchText = value;
                });
              },
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: viewModel.films.value
                  .where((film) => searchText.isEmpty ||
                  film.name.toLowerCase().contains(searchText.toLowerCase()))
                  .length,
              itemBuilder: (context, index) {
                final film = viewModel.films.value.where((film) =>
                searchText.isEmpty ||
                    film.name.toLowerCase().contains(searchText.toLowerCase())).toList()[index];
                return FilmRow(viewModel: FilmViewModel(film));
              },
            ),
          ),
          const Padding(
            padding: EdgeInsets.all(16.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
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
              ],
            ),
          ),
        ],
      ),
    );
  }
}
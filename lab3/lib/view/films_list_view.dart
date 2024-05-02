import 'package:flutter/material.dart';
import 'package:lab3/view/favored_view.dart';
import 'package:lab3/view/profile_view.dart';
import 'package:lab3/view/registration_view.dart';
import 'package:provider/provider.dart';

import '../viewmodel/film_viewmodel.dart';
import '../viewmodel/films_list_viewmodel.dart';
import 'film_row.dart';

class FilmsListView extends StatefulWidget {
  const FilmsListView({super.key});

  @override
  _FilmsListViewState createState() => _FilmsListViewState();
}

class _FilmsListViewState extends State<FilmsListView> {
  final viewModel = FilmsListViewModel();
  String searchText = '';

  @override
  void initState() {
    super.initState();
    viewModel.loadFilms();
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => viewModel,
      child: Consumer<FilmsListViewModel>(
        builder: (context, viewModel, child) {
          return Scaffold(
            body: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: TextField(
                    onChanged: (value) {
                      setState(() {
                        searchText = value;
                      });
                    },
                    decoration: InputDecoration(
                      hintText: 'Search',
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: ListView.builder(
                    itemCount: viewModel.films.length,
                    itemBuilder: (context, index) {
                      final film = viewModel.films[index];
                      if (searchText.isEmpty ||
                          film.name
                              .toLowerCase()
                              .contains(searchText.toLowerCase())) {
                        return FilmRow(viewModel: FilmViewModel(film));
                      } else {
                        return Container();
                      }
                    },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => const FavoredView(),
                            ),
                          );
                        },
                        child: const Text('Favored'),
                      ),
                      ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => ProfileView(),
                            ),
                          );
                        },
                        child: const Text('Profile'),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

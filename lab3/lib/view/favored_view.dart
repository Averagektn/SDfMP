import 'package:flutter/material.dart';
import 'package:lab3/view/films_list_view.dart';
import 'package:lab3/view/registration_view.dart';
import 'package:provider/provider.dart';

import '../viewmodel/favored_viewmodel.dart';
import '../viewmodel/film_viewmodel.dart';
import 'film_row.dart';

class FavoredView extends StatefulWidget {
  const FavoredView({super.key});

  @override
  _FavoredViewState createState() => _FavoredViewState();
}

class _FavoredViewState extends State<FavoredView> {
  final viewModel = FavoredViewModel();
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
      child: Consumer<FavoredViewModel>(
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
                              builder: (context) => const FilmsListView(),
                            ),
                          );
                        },
                        child: const Text('Films'),
                      ),
                      ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => RegistrationView(),
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

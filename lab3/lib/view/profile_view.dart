import 'package:flutter/material.dart';
import 'package:lab3/view/favored_view.dart';
import 'package:lab3/view/films_list_view.dart';
import 'package:lab3/view/registration_view.dart';

import '../viewmodel/profile_viewmodel.dart';

class ProfileView extends StatefulWidget {
  const ProfileView({super.key});

  @override
  _ProfileViewState createState() => _ProfileViewState();
}

class _ProfileViewState extends State<ProfileView> {
  final _viewModel = ProfileViewModel();

  @override
  void initState() {
    super.initState();
    _viewModel.addListener(() {
      setState(() {});
    });
    _viewModel.loadData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Row(
              children: [
                const Text(
                  'Email:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.email),
                    readOnly: true,
                    autocorrect: false,
                    autofocus: false,
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.exit_to_app, color: Colors.red),
                  onPressed: () => _showLogoutAlert(),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Login:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.login),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Country:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.country),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Favored film:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.favoredFilm),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Favored genre:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.favoredGenre),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Gender:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.gender),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Information:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.information),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Username:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.username),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Surname:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.surname),
                  ),
                ),
              ],
            ),
            Row(
              children: [
                const Text(
                  'Patronymic:',
                  style: TextStyle(fontSize: 18),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: TextField(
                    controller: TextEditingController(text: _viewModel.user.patronymic),
                  ),
                ),
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
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
                    _viewModel.update();
                  }, // Navigate to ProfileView
                  child: const Text('Update'),
                ),
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
              ],
            ),
          ],
        ),
      ),
    );
  }

  void _showLogoutAlert() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Log Out'),
          content: const Text('Are you sure you want to log out?'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
                onPressed: () {
                  _viewModel.deleteProfile();
                  Navigator.pop(context);
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(
                      builder: (context) => RegistrationView(),
                    ),
                  );
                },
                child: const Text('Delete')
            ),
            TextButton(
              onPressed: () {
                _viewModel.logout();
                Navigator.pop(context);
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(
                    builder: (context) => RegistrationView(),
                  ),
                );
              },
              child: const Text('Log Out'),
            ),
          ],
        );
      },
    );
  }
}
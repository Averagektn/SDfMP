import 'package:flutter/material.dart';
import 'package:lab3/viewmodel/registration_viewmodel.dart';

//import 'authorization_view.dart';
//import 'films_list_view.dart';

class RegistrationView extends StatefulWidget {
  const RegistrationView({super.key});

  @override
  _RegistrationViewState createState() => _RegistrationViewState();
}

class _RegistrationViewState extends State<RegistrationView> {
  final viewModel = RegistrationViewModel();

//  final authorizationViewModel = AuthorizationViewModel();
//  final filmsListViewModel = FilmsListViewModel();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Sign Up"),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(15.0),
        child: Column(
          children: [
            const Text(
              "Sign Up",
              style: TextStyle(fontSize: 40.0, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10.0),
            TextField(
              decoration: const InputDecoration(hintText: "Enter login"),
              onChanged: (value) => viewModel.login.add(value),
            ),
            const SizedBox(height: 10.0),
            TextField(
              decoration: const InputDecoration(hintText: "Enter email"),
              onChanged: (value) => viewModel.email.add(value),
              keyboardType: TextInputType.emailAddress,
            ),
            const SizedBox(height: 10.0),
            TextField(
              decoration: const InputDecoration(hintText: "Enter password"),
              onChanged: (value) => viewModel.password.add(value),
              obscureText: true,
            ),
            const SizedBox(height: 20.0),
/*            StreamBuilder<bool>(
              stream: viewModel.isShowFilmsListView,
              builder: (context, snapshot) {
                if (snapshot.hasData && snapshot.data!) {
                  return FilmsListView(viewModel: filmsListViewModel);
                } else {
                  return ElevatedButton(
                    onPressed: () => viewModel.registerUser(),
                    child: const Text("Sign Up"),
                  );
                }
              },
            ),*/
            const SizedBox(height: 10.0),
/*            TextButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => AuthorizationView(viewModel: authorizationViewModel),
                ),
              ),
              child: Text("Sign In"),
            ),*/
          ],
        ),
      ),
      resizeToAvoidBottomInset: false,
    );
  }

  @override
  void dispose() {
    viewModel.dispose();
    super.dispose();
  }
}

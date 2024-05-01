import 'package:flutter/material.dart';
import 'package:lab3/viewmodel/authorization_viewmodel.dart';
import 'registration_view.dart';

class AuthorizationView extends StatefulWidget {
  const AuthorizationView({super.key});

  @override
  createState() => _AuthorizationViewState();
}

class _AuthorizationViewState extends State<AuthorizationView> {
  final viewModel = AuthorizationViewModel();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Films browser"),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(15.0),
        child: Column(
          children: [
            const Text(
              "Sign In",
              style: TextStyle(fontSize: 40.0, fontWeight: FontWeight.bold),
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
                    onPressed: () => viewModel.authorization(),
                    child: Text("Sign In"),
                  );
                }
              },
            ),*/
            const SizedBox(height: 10.0),
            TextButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => const RegistrationView(),
                ),
              ),
              child: const Text("Sign Up"),
            ),
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

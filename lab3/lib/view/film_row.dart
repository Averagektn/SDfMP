import 'package:flutter/material.dart';

import '../viewmodel/film_viewmodel.dart';
import 'film_view.dart';

class FilmRow extends StatefulWidget {
  final FilmViewModel viewModel;

  const FilmRow({super.key, required this.viewModel});

  @override
  State<FilmRow> createState() => _FilmRowState();
}

class _FilmRowState extends State<FilmRow> {
  Image? image;

  @override
  void initState() {
    super.initState();
    widget.viewModel.loadImage();
    widget.viewModel.getImage().then((loadedImage) {
      setState(() {
        image = loadedImage;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => FilmView(film: widget.viewModel.film),
          ),
        );
      },
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Row(
          children: [
            if (image != null)
              Container(
                width: 100,
                height: 100,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: image!.image,
                    fit: BoxFit.fill,
                  ),
                ),
              )
            else
              const Icon(Icons.photo, size: 100, color: Colors.grey),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    widget.viewModel.film.name,
                    style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    widget.viewModel.film.categories.join(", "),
                    style: const TextStyle(fontSize: 14, color: Colors.grey),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
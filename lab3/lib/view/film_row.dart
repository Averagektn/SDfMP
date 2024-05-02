import 'package:flutter/material.dart';

import '../viewmodel/film_viewmodel.dart';
import 'film_view.dart';

class FilmRow extends StatefulWidget {
  final FilmViewModel viewModel;

  const FilmRow({super.key, required this.viewModel});

  @override
  createState() => _FilmRowState();
}

class _FilmRowState extends State<FilmRow> {
  ImageProvider? image;

  @override
  void initState() {
    super.initState();
    widget.viewModel.getImage().then((loadedImage) {
      setState(() {
        image = loadedImage;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        Navigator.pushReplacement(
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
            Container(
              width: 100,
              height: 100,
              decoration: BoxDecoration(
                color: Colors.grey,
                image: image != null
                    ? DecorationImage(image: image!, fit: BoxFit.cover)
                    : null,
              ),
              child: image == null
                  ? const Icon(Icons.photo, size: 50, color: Colors.white)
                  : null,
            ),
            const SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    widget.viewModel.film.name,
                    style: const TextStyle(
                        fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    widget.viewModel.film.categories.join(", "),
                    style: const TextStyle(fontSize: 14, color: Colors.grey),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    widget.viewModel.film.description,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
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

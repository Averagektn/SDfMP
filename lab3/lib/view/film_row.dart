import 'package:flutter/material.dart';

import '../viewmodel/film_viewmodel.dart';
import 'film_view.dart';

class FilmRow extends StatefulWidget {
  final FilmViewModel viewModel;

  const FilmRow({Key? key, required this.viewModel}) : super(key: key);

  @override
  _FilmRowState createState() => _FilmRowState();
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
                  ? Icon(Icons.photo, size: 50, color: Colors.white)
                  : null,
            ),
            SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    widget.viewModel.film.name,
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 4),
                  Text(
                    widget.viewModel.film.categories.join(", "),
                    style: TextStyle(fontSize: 14, color: Colors.grey),
                  ),
                  SizedBox(height: 4),
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
import 'package:flutter/material.dart';

import '../model/comment.dart';

class CommentView extends StatelessWidget {
  final Comment comment;

  const CommentView({super.key, required this.comment});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            comment.author,
            style: const TextStyle(fontSize: 16.0, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 4.0), // Add spacing between author and text
          Text(comment.text),
        ],
      ),
    );
  }
}
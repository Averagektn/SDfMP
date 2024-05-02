class Film {
  final String? id;
  final String name;
  final List<String> categories;
  final String description;

  Film({required this.name, required this.categories, required this.description, this.id});
}
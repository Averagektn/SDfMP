import Foundation

class Film: Identifiable {
    var id: String?
    var name: String
    var categories: [String]
    var description: String

    init(name: String, categories: [String], description: String) {
        self.name = name
        self.categories = categories
        self.description = description
    }
}

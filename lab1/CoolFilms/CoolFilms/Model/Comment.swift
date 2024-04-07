import Foundation

class Comment: Identifiable {
    var author: String
    var text: String

    init(author: String, comment: String) {
        self.author = author
        self.text = comment
    }
}

import SwiftUI

struct CommentView: View {
    @State private var comment: Comment
    
    init(comment: Comment) {
        self._comment = State(initialValue: comment)
    }
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(comment.author)
                .font(.headline)
            
            Text(comment.text)
        }
        .padding()
    }
}

import SwiftUI
import shared

struct RootView: View {
    private let root: RootComponent
    
    init(_ root: RootComponent) {
        self.root = root
    }
    
    var body: some View {
        StackView(
            stackValue: StateValue(root.childStack),
            getTitle: { _ in "" },
            onBack: root.onBackClicked
        ) { child in
            switch child {
            case let child as Pos: PosView(child.component)
            case let child as ProductDetails: ProductDetailsView(child.component)
            default: EmptyView()
            }
        }
    }
}

private typealias Pos = RootComponentChild.Pos
private typealias ProductDetails = RootComponentChild.ProductDetails

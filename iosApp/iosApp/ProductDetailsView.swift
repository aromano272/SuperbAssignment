import SwiftUI
import shared

struct ProductDetailsView : View {
    public typealias ViewModel = ViewModelWrapper<
        ProductDetailsComponent,
        ProductDetailsIntent,
        ProductDetailsViewState
    >
    
    @StateObject
    private var viewModel: ViewModel
    
    init(_ root: ProductDetailsComponent) {
        let wrapper: ViewModel = .init(viewModel: root)
        _viewModel = StateObject(wrappedValue: wrapper)
    }
    
    var body: some View {
        ComponentView(viewModel) { state, onIntent in
            ZStack {
                ScrollView {
                    if let error = state.error {
                        Text(error)
                    } else if let product = state.product {
                        VStack(alignment: .leading, spacing: 16) {
                            Text(product.name)
                                .font(.title)
                                .fontWeight(.bold)

                            if let category = product.category {
                                Text("\(Strings.shared.product_details_category_label) \(category.name)")
                                    .font(.subheadline)
                                    .foregroundColor(.blue)
                            }

                            Text("\(Strings.shared.product_details_price_label)\(String(format: "%.2f", Double(product.price) / 100))")
                                .font(.headline)

                            Text(product.desc ?? Strings.shared.product_details_no_description)
                                .font(.body)
                                .padding(.top, 8)

                            Spacer()
                        }
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }.refreshable {
                    onIntent(ProductDetailsIntentRefreshClicked())
                }

                if state.isLoading {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .scaleEffect(1.5)
                }
            }
        }
    }
    
}

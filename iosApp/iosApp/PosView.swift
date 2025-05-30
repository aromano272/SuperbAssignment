import SwiftUI
import shared

struct PosView : View {
    public typealias ViewModel = ViewModelWrapper<
        PosComponent,
        PosIntent,
        PosViewState
    >
    
    @StateObject
    private var viewModel: ViewModel
    
    init(_ root: PosComponent) {
        let wrapper: ViewModel = .init(viewModel: root)
        _viewModel = StateObject(wrappedValue: wrapper)
    }
    
    var body: some View {
        ComponentView(viewModel) { state, onIntent in
            ZStack {
                ScrollView {
                    VStack {
                        FilterRow(state: state, onIntent: onIntent)
                        
                        contentView(state: state, onIntent: onIntent)
                    }
                }.refreshable {
                    onIntent(PosIntentRefreshClicked())
                }
                
                if state.isLoading {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())
                        .scaleEffect(1.5)
                }
            }
        }
    }

    @ViewBuilder
    func productView(product: Product, onClick: @escaping () -> Void) -> some View {
        Button(action: {
            onClick()
        }) {
            HStack(alignment: .center) {
                VStack(alignment: .leading, spacing: 4) {
                    Text(product.name)
                        .font(.headline)

                    if let category = product.category {
                        Text(category.name)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                }

                Spacer()

                Text(String(format: "$%.2f", Double(product.price) / 100))
                    .font(.subheadline)
                    .foregroundColor(.blue)
            }
            .padding()
            .background(Color(.systemGray6))
            .cornerRadius(10)
        }
        .buttonStyle(PlainButtonStyle())
    }

    @ViewBuilder
    func contentView(
        state: PosViewState,
        onIntent: @escaping (PosIntent) -> Void
    ) -> some View {
        if let error = state.error {
            Text(error)
        } else {
            LazyVStack {
                ForEach(state.products ?? [], id: \.id) { product in
                    productView(product: product) {
                        onIntent(PosIntentProductClicked(id: product.id))
                    }
                }
            }
            .padding()
        }
    }
    
    struct FilterRow: View {
        let state: PosViewState
        let onIntent: (PosIntent) -> Void

        @State private var isPickerOpen = false

        var body: some View {
            HStack {
                Menu {
                    ForEach(state.availableSortStrategies, id: \.self) { option in
                        Button(option.label) {
                            onIntent(PosIntentSortStrategyChanged(sortStrategy: option))
                        }
                    }
                } label: {
                    Label(
                        title: { Text(state.selectedSortStrategy.label) },
                        icon: { Image(systemName: "line.3.horizontal.decrease.circle") }
                    )
                }

                Spacer()

                HStack {
                    Text(Strings.shared.pos_price_filter_label)

                    TextField(
                        "",
                        text: Binding(
                            get: { state.priceFilterInput },
                            set: { onIntent(PosIntentPriceFilterInputChanged(input: $0)) }
                        )
                    )
                    .keyboardType(.decimalPad)
                    .textFieldStyle(.roundedBorder)
                    .foregroundColor(state.priceFilterInputError ? .red : .primary)
                    .frame(width: 100)
                }
            }
            .padding(.horizontal)
        }
    }

}

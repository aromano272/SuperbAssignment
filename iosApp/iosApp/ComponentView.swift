import SwiftUI
import shared

public struct ComponentView<
    WrapperViewModel,
    WrapperIntent,
    WrapperViewState,
    Content: View
>: View where
WrapperViewModel: ViewModelComponent,
WrapperIntent: Intent,
WrapperViewState: ViewState & Equatable {
    
    public typealias Wrapper = ViewModelWrapper<
        WrapperViewModel,
        WrapperIntent,
        WrapperViewState
    >
    
    @ObservedObject
    private var viewModelWrapper: ViewModelWrapper<
        WrapperViewModel,
        WrapperIntent,
        WrapperViewState
    >
    
    private let content: (WrapperViewState, @escaping (WrapperIntent) -> Void) -> Content
    
    public init(
        _ viewModel: StateObject<Wrapper>,
        @ViewBuilder _ content: @escaping (WrapperViewState, @escaping (WrapperIntent) -> Void) -> Content
    ) {
        self.viewModelWrapper = viewModel.wrappedValue
        self.content = content
    }
    
    public init(
        _ viewModel: Wrapper,
        @ViewBuilder _ content: @escaping (WrapperViewState, @escaping (WrapperIntent) -> Void) -> Content
    ) {
        self.viewModelWrapper = viewModel
        self.content = content
    }
    
    @State private var showAlert: Bool = false
    @State private var alertTitle: String = ""
    @State private var alertMessage: String = ""
    
    public var body: some View {
        let state = viewModelWrapper.viewState
        let commonState = (state as? ViewStateWithCommonState)?.commonState
        
        content(state, viewModelWrapper.onIntent)
            .ifLet(commonState?.topBarViewState) { view, topBar in view.navigationTitle(topBar.title) }
            .navigationBarTitleDisplayMode(.inline)
            .alert(isPresented: $showAlert) {
                Alert(
                    title: Text(alertTitle),
                    message: Text(alertMessage),
                    dismissButton: .default(Text("OK"))
                )
            }
            .onChange(of: commonState?.errorAlert) { newError in
                if let message = newError {
                    alertTitle = Strings.shared.alert_error_title
                    alertMessage = message
                    showAlert = true
                }
            }
            .onChange(of: commonState?.successAlert) { newSuccess in
                if let message = newSuccess {
                    alertTitle = Strings.shared.alert_success_title
                    alertMessage = message
                    showAlert = true
                }
            }
    }
    
}

extension View {
    @ViewBuilder
    func `if`<Content: View>(
        _ condition: Bool,
        apply: (Self) -> Content
    ) -> some View {
        if condition {
            apply(self)
        } else {
            self
        }
    }
    
    @ViewBuilder
    func ifLet<T, Content: View>(
        _ value: T?,
        apply: (Self, T) -> Content
    ) -> some View {
        if let unwrapped = value {
            apply(self, unwrapped)
        } else {
            self
        }
    }
}

import Combine
import shared
import SwiftUI

/// Wraps any ViewModel to be able to observe any ViewState changes
/// so SwiftUI can react to those updates.
///
/// As we are using some naming conventions for the ViewModel types,
/// wrapper can be defines doing the following for `SomeFeature`:
///
/// ```swift
/// typealias SomeFeatureViewModel = ViewModelWrapper<
///     SomeFeatureViewModel,
///     SomeFeatureIntent,
///     SomeFeatureViewState
/// >
/// ```
/// In order to initialize the wrapper, we need to provide an specific
/// implementation for the ViewModel that satifies all the types constrains.
///
/// ```swift
/// @ObservedObject
/// private var viewModel: SomeFeatureViewModel
/// ```
///
public final class ViewModelWrapper<WrapperViewModel, WrapperIntent, WrapperViewState>: ObservableObject
where WrapperViewModel: ViewModelComponent, WrapperIntent: Intent, WrapperViewState: ViewState {
    public typealias ViewState = WrapperViewState
    
    let _viewModel: ViewModelComponent?
    public let viewModel: WrapperViewModel?
    

    /// View state property that will be updated when the internal view model
    /// emits a new view state
    ///
    @Published public private(set) var viewState: WrapperViewState

    // ViewModelWrapper initializer
    //
    // The inner viewModel will not be invoked at any time.
    //
    // - Parameter viewModel: The KMP viewModel that needs to be wrapped.
    //
    public init(
        viewModel: @autoclosure () -> ViewModelComponent?
    ) {
        _viewModel = viewModel()

        // If the KMP viewModel's view state is not a WrapperViewState instance, we
        // should crash the app, that's an invalid state because the expected types
        // and the real ones are not matching.
        //
        // To avoid runtime crashes, we are providing unit test to ensure that we are
        // able to build ViewModelWrapper instances with the expected types.
        //
        viewState = _viewModel?.state.value as! WrapperViewState
        self.viewModel = (_viewModel as! WrapperViewModel)

        if let _viewModel {
            Task {
                for await viewState in _viewModel.state {
                    await MainActor.run { [weak self] in
                        guard let self else { return }
                        guard let wrapperViewState = viewState as? WrapperViewState else { return }
                        self.viewState = wrapperViewState
                    }
                }
            }
        }
    }

    public func onIntent(intent: WrapperIntent) {
        _viewModel?.onIntent(intent: intent)
    }
    
    /// Provides a SwiftUI binding to a particular ViewState property.
    ///
    /// The following example represents a binding to a ViewState property
    /// named `email` and any text input will be mapped as a `SampleIntent.EmailUpdated`
    /// intent.
    ///
    /// ```swift
    /// TextField(
    ///     "Email",
    ///     text: viewModel.binding(
    ///         \.email,
    ///         onUpdate: SampleIntent.EmailUpdated.init(email:)
    ///     )
    /// )
    /// ```
    /// - Parameter value: ViewState KeyPath representing the ViewState property that matches
    /// the expected Binding value type.
    /// - Parameter onUpdate: Closure that will generate an intent that wraps the produced value.
    ///
    public func binding<T>(_ value: KeyPath<WrapperViewState, T>, onUpdate: @escaping ((T) -> WrapperIntent)) -> Binding<T> {
        Binding(
            get: { [viewState] in viewState[keyPath: value] },
            set: { [weak self] in
                guard let self else { return }
                self._viewModel?.onIntent(intent: onUpdate($0))
            }
        )
    }
    
}

extension ViewState {
    public func binding<T>(_ value: KeyPath<Self, T>, onUpdate: ((T) -> Void)? = nil) -> Binding<T> {
        Binding(
            get: { self[keyPath: value] },
            set: { onUpdate?($0) }
        )
    }
}

//
//  ContentView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/14/25.
//

import SwiftUI
import Shared

enum Route: Hashable {
    case login
    case rideList
    case rideDetail(ZwiftPowerRide)
}

struct ContentView: View {
    @State private var path: [Route] = []
    @State private var cookies: [HTTPCookie] = []
    @State private var rides: [ZwiftPowerRide] = []
    @State private var showWebView = false

    var body: some View {
        NavigationStack(path: $path) {
            VStack {
                if showWebView {
                    LoginView(onCookiesExtracted: { cookieMap in
                        self.cookies = []
                        self.showWebView = false
                        self.path = [.rideList]
                    })
                } else {
                    Button("Log in to ZwiftPower") {
                        self.showWebView = true
                    }
                    .padding()
                }
            }
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .login:
                    EmptyView() // no-op, shouldn't navigate to login
                case .rideList:
                    RideListView(cookies: cookies, onSelect: { ride in
                        path.append(.rideDetail(ride))
                    })
                case .rideDetail(let ride):
                    RideDetailView(ride: ride)
                }
            }
        }
    }
}

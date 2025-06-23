//
//  ContentView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/14/25.
//

import SwiftUI
import Shared
import os
private let logger = Logger(subsystem: "com.danielchew.zwiftviewer", category: "ZwiftDebug")

enum Route: Hashable {
    case login
    case rideList
    case rideDetail(Ride)
}

struct ContentView: View {
    @State private var route: Route? = .login
    @State private var savedCookies: [String: String] = [:]

    var body: some View {
        NavigationStack {
            switch route {
            case .login, .none:
                LoginView(onCookiesExtracted: { cookiesMap in
                    if let profileUrl = cookiesMap["profileUrl"], profileUrl.contains("?z=") {
                        logger.info("ContentView: Valid Zwift profile URL received = \(profileUrl)")
                        savedCookies = cookiesMap
                        route = .rideList
                    } else {
                        logger.error("LoginView: profileUrl missing or invalid in cookies map.")
                    }
                })
            case .rideList:
                RideListView(
                    cookies: savedCookies.compactMap {
                        HTTPCookie(properties: [
                            .name: $0.key,
                            .value: $0.value,
                            .domain: "zwiftpower.com",
                            .path: "/",
                            .version: "0"
                        ])
                    },
                    onSelect: { ride in
                        route = .rideDetail(ride)
                    },
                    onBack: {
                        route = .login
                    }
                )
            case .rideDetail(let ride):
                RideDetailView(ride: ride, onBack: {
                    route = .rideList
                })
            }
        }
    }
}

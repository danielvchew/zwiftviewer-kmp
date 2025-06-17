//
//  ContentView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/14/25.
//

import SwiftUI
import Shared

struct ContentView: View {
    @State private var cookies: [String: String]? = nil
    @State private var rides: [ZwiftPowerRideViewModel] = []

    var body: some View {
        if cookies == nil {
            ZwiftPowerLoginView { extractedCookies in
                self.cookies = extractedCookies
                print("ZwiftDebug: Extracted cookies = \(extractedCookies)")
            }
        } else {
            VStack {
                Text("Logged in successfully!")
                    .font(.title)
                    .padding()

                Button("Load Rides") {
                    Task {
                        let client = KtorClientProvider.shared.provideAuthenticatedClient(cookies: cookies ?? [:])
                        let profileId = (try? await ZwiftPowerAuthUtilsKt.extractZwiftPowerUserId(client: client)) ?? "UNKNOWN"
                        print("ZwiftDebug: iOS Parsed ZwiftPower user ID = \(profileId)")

                        let rideApi = RideApiBridge()
                        do {
                            let rides = try await rideApi.getRides(profileId: profileId, cookies: cookies ?? [:])
                            self.rides = rides.map { ZwiftPowerRideViewModel($0) }
                        } catch {
                            print("ZwiftDebug: Failed to get rides: \(error.localizedDescription)")
                        }
                    }
                }
                
                List(rides, id: \.zid) { ride in
                    VStack(alignment: .leading) {
                        Text("🏁 Title: \(ride.title)")
                        Text("📏 Distance: \(ride.distance) km")
                        Text("⚡️ Avg Power: \(ride.avgPower) W")
                    }
                    .padding(.vertical, 4)
                }
            }
        }
    }
}

#Preview {
    ContentView()
}

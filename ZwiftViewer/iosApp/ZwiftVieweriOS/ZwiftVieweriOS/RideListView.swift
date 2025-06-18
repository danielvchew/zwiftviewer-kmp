//
//  RideListView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/17/25.
//

import SwiftUI
import Shared

struct RideListView: View {
    let cookies: [String: String]
    let profileId: String
    @State private var rides: [ZwiftPowerRideViewModel] = []

    var body: some View {
        List(rides, id: \.zid) { ride in
            VStack(alignment: .leading) {
                Text("Zwift Ride: \(ride.title)")
                    .font(.headline)
                Text("Distance: \(ride.distance) km")
                Text("Avg Power: \(ride.avgPower) W")
            }
            .padding(.vertical, 4)
        }
        .onAppear {
            print("ZwiftDebug: Calling getRides with profileId = \(profileId)")
            print("ZwiftDebug: Cookies = \(cookies)")
            Task {
                do {
                    let rawRides = try await RideApiBridge().getRides(profileId: profileId, cookies: cookies)
                    self.rides = rawRides.map { ZwiftPowerRideViewModel($0) }
                } catch {
                    print("ZwiftDebug: Failed to load rides — \(error)")
                }
            }
        }
    }
}

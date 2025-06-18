//
//  RideDetailView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/18/25.
//

import SwiftUI
import Shared // for ZwiftPowerRide

struct RideDetailView: View {
    let ride: ZwiftPowerRide

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("🏁 \(ride.title)")
                .font(.title2)
                .bold()

            Text("Distance: \(Double(ride.distance) / 1000.0, specifier: "%.1f") km")
            if let power = ride.avgPower.first {
                Text("Avg Power: \(power) W")
            }

            if let hr = ride.avgHr.first {
                Text("Avg HR: \(hr) bpm")
            }

            if let cadence = ride.avgCadence.first {
                Text("Avg Cadence: \(cadence) rpm")
            }

            Text("Calories: \(ride.calories)")
            Text("Sport: \(ride.sport)")
            Text("World ID: \(ride.worldId)")
        }
        .padding()
        .navigationTitle("Ride Details")
        .navigationBarTitleDisplayMode(.inline)
    }
}

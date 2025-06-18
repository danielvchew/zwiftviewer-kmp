//
//  RideListView.swift
//  ZwiftVieweriOS
//
//  Created by Daniel Chew on 6/17/25.
//

import SwiftUI
import Shared

struct RideListView: View {
    let cookies: [HTTPCookie]
    let onSelect: (ZwiftPowerRide) -> Void

    @State private var rides: [ZwiftPowerRide] = []
    @State private var isLoading: Bool = true
    @State private var error: String?

    var body: some View {
        VStack(alignment: .leading) {
            Text("Ride List")
                .font(.title2)
                .bold()
                .padding(.bottom, 8)

            if isLoading {
                ProgressView("Loading rides...")
            } else if let error = error {
                Text("Error: \(error)")
                    .foregroundColor(.red)
            } else {
                List(rides, id: \.zid) { ride in
                    Button(action: {
                        onSelect(ride)
                    }) {
                        VStack(alignment: .leading) {
                            Text(ride.title)
                                .font(.headline)
                            if let power = ride.avgPower.first {
                                Text("Avg Power: \(power) W")
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
                .listStyle(PlainListStyle())
            }
        }
        .padding()
        .onAppear {
            self.rides = [
                ZwiftPowerRide(
                    date: 1720000000000,
                    zaid: "mockZaid",
                    title: "Mock Ride 1",
                    zid: "mockZid",
                    elapsed: [3600],
                    distance: 25000,
                    worldId: "1",
                    sport: "cycling",
                    fit: "fit789",
                    aid: "aid999",
                    avgSpeed: 38,
                    avgHr: [140],
                    maxHr: [180],
                    avgCadence: [85],
                    calories: 600,
                    avgPower: [185],
                    elevation: 300,
                    zeid: 1234
                )
            ]
            self.isLoading = false
        }
    }
}

# CoastGuard

This is a similation for an AI Agent, using Java and Prolog, mimicking the role of a coast guard saving passengers from wrecks, implementing search algorithms, such as A* Search and Greedy Search to minimize the cost.

# Summary
You are a member of the coast guard force in charge of a rescue
boat that goes into the sea to rescue other sinking ships. When rescuing a ship, you need
to rescue any living people on it and to retrieve its black box after there are no more
passengers thereon to rescue. If a ship sinks completely, it becomes a wreck and you still
have to retrieve the black box before it is damaged. Each ship loses one passenger every
time step. Additionally, each black box incurs an additional damage point every time
step once the ship becomes a wreck. One time step is counted every time an action is
performed. You reach your goal when there are no living passengers who are not rescued,
there are no undamaged boxes which have not been retrieved, and the rescue boat is not
carrying any passengers.  You would also like to rescue as many people as possible and
retrieve as many black boxes as possible.

# Elements

• **Coast Guard Boat**: The coast guard boat is the agent; it is the only element that
can move on the grid. The coast guard can enter any cell. It has a fixed capacity
30 ≤ c ≤ 100 which indicates the number of passengers it can carry at one time.
So it may have to make multiple visits to one ship to save all the passengers on it.
• **Ships**: Each ship has a certain number of passengers and, at every time step, one
of them expires. The ship is considered sunk when all of them are expired or all are
picked up In that case, it becomes a wreck. Each ship contains a black box that
can be retrieved after it sinks as long as it has not been completely damaged.
• **Wrecks**: Once the ship no longer has any passengers (all expire or all are picked
up), it becomes a wreck. When the ship becomes a wreck (once its last passenger
either expires or is picked up), in the next time step, the black box starts counting
damage from 1 all the way up to 20. Once damage reaches 20, the black box is no
longer retrievable.
• **Stations**: Stations are fixed and do not have any capacity limits. To count a passenger saved, they need to be dropped at a station.

# Configuration

Initially, the grid is configured as follows:

• The coast guard boat is at a random location
• Several ships are scattered at random locations, and each has a random initial
number of passengers p, where 0 < p ≤ 100.
• Several stations are at random locations.
• There are no wrecks.
• No two items are in the same cell.

# Actions

• **Pick-up**: The coast guard picks up as many passengers off a ship as its remaining
capacity allows. This can be done on a ship that is in the same cell as the coast
guard and it only affects this ship. Once a passenger is picked up by the coast
guard, they will not expire and will stay on the coast guard boat until they are
dropped at a station.
• **Drop**: The coast guard drops all passengers it is currently carrying at a station.
This can only be done when the coast guard and the station are in the same cell
and it resets the remaining capacity of the coast guard boat to 0.
• **Retrieve**: The coast guard boat retrieves a black box. This can only be done when
the coast guard boat is in the same cell as a wreck with a black box which has
not been completely damaged yet. This action does not affect the coast guard’s
remaining capacity at all.
• **Movement** in any of the 4 directions (up, down, left, right) within the grid
boundaries.

# Search Algorithms

In this project, you will create an agent that acts as the coast guard boat captain
using search. The agent has succeeded when there are no more un-expired passengers
to save and no more undamaged black boxes to retrieve. Optimally, you would like to
minimise the number of expired passengers and secondarily retrieve as many black boxes
as possible. Several algorithms will be implemented and each will be used to play the
game:
1. Breadth-first search.
2. Depth-first search.
3. Iterative deepening search.
4. Greedy search with at least two heuristics.
5. A∗ search with at least two admissible, non-trivial heuristics.

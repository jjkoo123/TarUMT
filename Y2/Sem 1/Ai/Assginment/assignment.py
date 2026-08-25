# State format:
# (missionaries_left, cannibals_left, boat_position,
#  total_missionaries, total_cannibals, boat_capacity)
from time import perf_counter

def is_valid(state):
    """Check whether a state follows all safety rules."""

    m_left, c_left, boat, total_m, total_c, capacity = state

    m_right = total_m - m_left
    c_right = total_c - c_left

    # Number of people cannot be negative or exceed the total
    if not (0 <= m_left <= total_m):
        return False

    if not (0 <= c_left <= total_c):
        return False

    # Missionaries on the left bank cannot be outnumbered
    if m_left > 0 and c_left > m_left:
        return False

    # Missionaries on the right bank cannot be outnumbered
    if m_right > 0 and c_right > m_right:
        return False

    return True


def is_goal(state):
    """Check whether everyone has reached the right bank."""

    m_left, c_left, boat, total_m, total_c, capacity = state

    return m_left == 0 and c_left == 0 and boat == "R"


def generate_boat_moves(capacity):
    """Generate all possible passenger combinations."""

    moves = []

    for missionaries in range(capacity + 1):
        for cannibals in range(capacity + 1):

            passengers = missionaries + cannibals

            # Boat must carry at least one person
            if 1 <= passengers <= capacity:
                moves.append((missionaries, cannibals))

    return moves


def get_next_states(state):
    """Generate every valid state reachable from the current state."""

    m_left, c_left, boat, total_m, total_c, capacity = state
    next_states = []

    moves = generate_boat_moves(capacity)

    for missionaries, cannibals in moves:

        if boat == "L":
            # Check whether enough people are available on the left
            if missionaries > m_left or cannibals > c_left:
                continue

            new_state = (
                m_left - missionaries,
                c_left - cannibals,
                "R",
                total_m,
                total_c,
                capacity
            )

        else:
            m_right = total_m - m_left
            c_right = total_c - c_left

            # Check whether enough people are available on the right
            if missionaries > m_right or cannibals > c_right:
                continue

            new_state = (
                m_left + missionaries,
                c_left + cannibals,
                "L",
                total_m,
                total_c,
                capacity
            )

        if is_valid(new_state):
            next_states.append(
                (new_state, (missionaries, cannibals))
            )

    return next_states


def display_state(state):
    """Display a state in an understandable format."""

    m_left, c_left, boat, total_m, total_c, capacity = state

    m_right = total_m - m_left
    c_right = total_c - c_left

    return (
        f"Left: {m_left}M {c_left}C | "
        f"Boat: {boat} | "
        f"Right: {m_right}M {c_right}C"
    )


# Temporary test
total_missionaries = 12
total_cannibals = 12
boat_capacity = 4

initial_state = (
    total_missionaries,
    total_cannibals,
    "L",
    total_missionaries,
    total_cannibals,
    boat_capacity
)

print("Initial state:")
print(display_state(initial_state))

print("\nPossible next states:")

for next_state, move in get_next_states(initial_state):
    missionaries, cannibals = move

    print(
        f"Move {missionaries}M and {cannibals}C: "
        f"{display_state(next_state)}"
    )
    
    
    
#=================================================================================================================#

#aglorithm

def depth_first_search(initial_state):
    """Solve the problem using Depth-First Search (DFS)."""

    start_time = perf_counter()

    # Stack = states waiting to be explored by DFS
    stack = [initial_state]

    # Prevent the algorithm from revisiting the same state forever
    visited = {initial_state}

    # Save where each state came from and the move used
    parent = {
        initial_state: (None, None)
    }

    states_expanded = 0
    max_frontier_size = 1

    while stack:
        current_state = stack.pop()
        states_expanded += 1

        # Stop when everyone reaches the right bank
        if is_goal(current_state):
            path = []
            state = current_state

            # Rebuild the solution from goal back to initial state
            while state is not None:
                previous_state, move = parent[state]
                path.append((state, move))
                state = previous_state

            path.reverse()

            return {
                "found": True,
                "path": path,
                "moves": len(path) - 1,
                "states_expanded": states_expanded,
                "unique_states_seen": len(visited),
                "max_frontier_size": max_frontier_size,
                "execution_time": perf_counter() - start_time
            }

        successors = get_next_states(current_state)

        # Reversed keeps the DFS order consistent with generated moves
        for next_state, move in reversed(successors):
            if next_state not in visited:
                visited.add(next_state)
                parent[next_state] = (current_state, move)
                stack.append(next_state)

        max_frontier_size = max(max_frontier_size, len(stack))

    # DFS explored all possible legal states without reaching the goal
    return {
        "found": False,
        "path": [],
        "moves": 0,
        "states_expanded": states_expanded,
        "unique_states_seen": len(visited),
        "max_frontier_size": max_frontier_size,
        "execution_time": perf_counter() - start_time
    }


def print_dfs_result(result):
    """Display the DFS solution in table format."""

    print("\n" + "=" * 98)
    print("                 MISSIONARIES AND CANNIBALS — DFS RESULT")
    print("=" * 98)

    if not result["found"]:
        print("No solution found.")
        print("=" * 98)
        return

    print(f"Solution found in {result['moves']} boat trips.\n")

    print(
        f"{'Step':<6}"
        f"{'Move':<32}"
        f"{'Left Bank':<18}"
        f"{'Boat':<10}"
        f"{'Right Bank':<18}"
    )
    print("-" * 98)

    for step, (state, move) in enumerate(result["path"]):
        m_left, c_left, boat, total_m, total_c, capacity = state
        m_right = total_m - m_left
        c_right = total_c - c_left

        left_bank = f"{m_left}M, {c_left}C"
        right_bank = f"{m_right}M, {c_right}C"

        if step == 0:
            move_text = "Initial state"
        else:
            previous_state = result["path"][step - 1][0]
            missionaries, cannibals = move

            direction = (
                "Left → Right"
                if previous_state[2] == "L"
                else "Right → Left"
            )

            move_text = f"{missionaries}M, {cannibals}C ({direction})"

        print(
            f"{step:<6}"
            f"{move_text:<32}"
            f"{left_bank:<18}"
            f"{boat:<10}"
            f"{right_bank:<18}"
        )

    print("-" * 98)
    print("Performance Summary")
    print("-" * 98)
    print(f"{'Boat trips':<25}: {result['moves']}")
    print(f"{'States expanded':<25}: {result['states_expanded']}")
    print(f"{'Unique states seen':<25}: {result['unique_states_seen']}")
    print(f"{'Maximum stack size':<25}: {result['max_frontier_size']}")
    print(f"{'Execution time':<25}: {result['execution_time']:.6f} seconds")
    print("=" * 98)





# Test data
total_missionaries = 12
total_cannibals = 12
boat_capacity = 4

initial_state = (
    total_missionaries,
    total_cannibals,
    "L",
    total_missionaries,
    total_cannibals,
    boat_capacity
)

print("Initial state:")
print(display_state(initial_state))

result = depth_first_search(initial_state)
print_dfs_result(result)









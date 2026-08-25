# Gig-Match — Individual Profile Module

This Android Studio project is written in Kotlin with Jetpack Compose. It contains one Profile page for the assigned individual scope while retaining the team's required bottom navigation exactly as supplied:

1. Find Work
2. Post Job
3. Company Detail
4. Profile

The Profile item is selected because this project implements only the student's assigned Profile module. The other three destinations are intentionally left for team integration.

## Functions implemented on the one Profile page

### 1. User requests a job from an employer

- Displays the job, company, location, schedule, pay, and description.
- **Request This Job** sends the request.
- The button changes to **Request Sent to Employer**.
- A **Pending** status is displayed.
- Request status persists after restarting the app.

### 2. Skill badge system

- Displays earned and in-progress badges.
- Includes Verified Worker, Top Rated, Reliable, Punctual, Customer Service, Quick Learner, and Team Player.
- Badges can be awarded from an employer review.
- Awarding a badge updates the Profile page immediately.

### 3. Review and rating

- Displays the worker's calculated average rating and total number of reviews.
- Displays recent employer reviews with full and half stars.
- **Write a Review** allows an employer to enter company name, job title, 1–5 stars, a comment, and one optional skill badge.
- New reviews and ratings persist after restarting the app.

## Reference-matching UI

- Navy headings
- Pale grey background
- White rounded cards with light-blue borders
- Green rating and verified-worker accents
- Worker summary and review-history layout
- Bottom navigation with Profile icon and label inside the blue selected area

## Open and run

1. Open the `GigMatch` folder in Android Studio.
2. Wait for Gradle sync.
3. Select an emulator or connected Android device.
4. Press **Run**.

Minimum Android version: Android 7.0 (API 24).

## Important source files

- `MainActivity.kt` — exact four-item bottom navigation and Profile module entry.
- `data/Models.kt` — Job, Review, and SkillBadge models.
- `data/GigMatchRepository.kt` — persistent job request, ratings, reviews, and badge rules.
- `ui/components/Common.kt` — shared reference-style UI and bottom navigation.
- `ui/screens/ReviewsScreen.kt` — all three assigned functions on one page.
- `ui/theme/Theme.kt` — colours and typography.

## Suggested final Firebase tables/collections

| Collection | Main fields |
|---|---|
| `jobRequests` | `requestId`, `jobId`, `workerId`, `employerId`, `status`, `requestedAt` |
| `reviews` | `reviewId`, `jobId`, `reviewerId`, `workerId`, `rating`, `comment`, `badgeId`, `createdAt` |
| `userBadges` | `userBadgeId`, `userId`, `badgeId`, `reviewId`, `awardedAt` |

The prototype uses local Android storage so it can run immediately. During team integration, the repository can be replaced with Cloud Firestore operations.

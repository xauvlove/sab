import 'package:flutter_test/flutter_test.dart';
import 'package:sleep_story_app/main.dart';

void main() {
  testWidgets('App starts', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const SleepStoryApp());
  });
}

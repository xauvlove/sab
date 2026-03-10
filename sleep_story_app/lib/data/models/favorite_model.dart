import 'package:equatable/equatable.dart';

/// 收藏实体
class Favorite extends Equatable {
  final String id;
  final String userId;
  final String storyId;
  final String? storyTitle;
  final String? storyCoverUrl;
  final String? storyAudioUrl;
  final int? storyDuration;
  final DateTime createdAt;

  const Favorite({
    required this.id,
    required this.userId,
    required this.storyId,
    this.storyTitle,
    this.storyCoverUrl,
    this.storyAudioUrl,
    this.storyDuration,
    required this.createdAt,
  });

  factory Favorite.fromJson(Map<String, dynamic> json) {
    return Favorite(
      id: json['id'] ?? '',
      userId: json['userId'] ?? json['user_id'] ?? '',
      storyId: json['storyId'] ?? json['story_id'] ?? '',
      storyTitle: json['storyTitle'],
      storyCoverUrl: json['storyCoverUrl'],
      storyAudioUrl: json['storyAudioUrl'],
      storyDuration: json['storyDuration'] ?? json['story_duration'],
      createdAt: json['createdAt'] != null
          ? DateTime.parse(json['createdAt'])
          : DateTime.now(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'userId': userId,
      'storyId': storyId,
      'storyTitle': storyTitle,
      'storyCoverUrl': storyCoverUrl,
      'storyAudioUrl': storyAudioUrl,
      'storyDuration': storyDuration,
      'createdAt': createdAt.toIso8601String(),
    };
  }

  @override
  List<Object?> get props => [
        id,
        userId,
        storyId,
        storyTitle,
        storyCoverUrl,
        storyAudioUrl,
        storyDuration,
        createdAt,
      ];
}

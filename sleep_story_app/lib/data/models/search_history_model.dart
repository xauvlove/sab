/// 搜索历史模型
class SearchHistoryModel {
  final String id;
  final String keyword;
  final DateTime searchedAt;

  SearchHistoryModel({
    required this.id,
    required this.keyword,
    required this.searchedAt,
  });

  factory SearchHistoryModel.fromJson(Map<String, dynamic> json) {
    return SearchHistoryModel(
      id: json['id']?.toString() ?? '',
      keyword: json['keyword']?.toString() ?? '',
      searchedAt: json['searchedAt'] != null
          ? DateTime.tryParse(json['searchedAt'].toString()) ?? DateTime.now()
          : DateTime.now(),
    );
  }
}

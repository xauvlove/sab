import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../../data/models/story_model.dart';
import '../../providers/home_provider.dart';
import '../../widgets/story_card.dart';

/// 发现页面（搜索）
class DiscoveryScreen extends StatefulWidget {
  const DiscoveryScreen({super.key});

  @override
  State<DiscoveryScreen> createState() => _DiscoveryScreenState();
}

class _DiscoveryScreenState extends State<DiscoveryScreen> {
  final _searchController = TextEditingController();
  String _searchKeyword = '';
  bool _isSearching = false;
  List<Story> _searchResults = [];

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _search() async {
    if (_searchKeyword.isEmpty) return;

    setState(() => _isSearching = true);

    try {
      final results = await context.read<HomeProvider>().searchStories(_searchKeyword);
      setState(() {
        _searchResults = results;
        _isSearching = false;
      });
    } catch (e) {
      setState(() => _isSearching = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('搜索失败: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Column(
          children: [
            // 搜索栏
            Padding(
              padding: const EdgeInsets.all(16),
              child: TextField(
                controller: _searchController,
                decoration: InputDecoration(
                  hintText: '搜索故事...',
                  prefixIcon: const Icon(Icons.search),
                  suffixIcon: _searchKeyword.isNotEmpty
                      ? IconButton(
                          icon: const Icon(Icons.clear),
                          onPressed: () {
                            _searchController.clear();
                            setState(() {
                              _searchKeyword = '';
                              _searchResults = [];
                            });
                          },
                        )
                      : null,
                ),
                onChanged: (value) {
                  setState(() => _searchKeyword = value);
                },
                onSubmitted: (_) => _search(),
                textInputAction: TextInputAction.search,
              ),
            ),

            // 搜索按钮
            if (_searchKeyword.isNotEmpty)
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                child: SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: _isSearching ? null : _search,
                    child: _isSearching
                        ? const SizedBox(
                            height: 20,
                            width: 20,
                            child: CircularProgressIndicator(strokeWidth: 2),
                          )
                        : const Text('搜索'),
                  ),
                ),
              ),

            const SizedBox(height: 16),

            // 搜索结果
            Expanded(
              child: _searchKeyword.isEmpty
                  ? _buildSearchHints()
                  : _searchResults.isEmpty
                      ? Center(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Icon(
                                Icons.search_off,
                                size: 64,
                                color: Colors.grey[600],
                              ),
                              const SizedBox(height: 16),
                              Text(
                                _isSearching ? '搜索中...' : '未找到相关故事',
                                style: TextStyle(color: Colors.grey[600]),
                              ),
                            ],
                          ),
                        )
                      : ListView.builder(
                          padding: const EdgeInsets.symmetric(horizontal: 16),
                          itemCount: _searchResults.length,
                          itemBuilder: (context, index) {
                            final story = _searchResults[index];
                            return Padding(
                              padding: const EdgeInsets.only(bottom: 12),
                              child: StoryCard(
                                story: story,
                                onTap: () {
                                  // TODO: 跳转到播放页面
                                },
                              ),
                            );
                          },
                        ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSearchHints() {
    final categories = [
      {'name': '失眠', 'icon': Icons.bedtime},
      {'name': '冥想', 'icon': Icons.self_improvement},
      {'name': '儿童', 'icon': Icons.child_care},
      {'name': '放松', 'icon': Icons.spa},
      {'name': '自然', 'icon': Icons.park},
      {'name': '轻音乐', 'icon': Icons.music_note},
    ];

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '热门搜索',
            style: Theme.of(context).textTheme.titleMedium,
          ),
          const SizedBox(height: 16),
          Wrap(
            spacing: 8,
            runSpacing: 8,
            children: categories.map((cat) {
              return ActionChip(
                avatar: Icon(cat['icon'] as IconData, size: 18),
                label: Text(cat['name'] as String),
                onPressed: () {
                  _searchController.text = cat['name'] as String;
                  setState(() => _searchKeyword = cat['name'] as String);
                  _search();
                },
              );
            }).toList(),
          ),
          const SizedBox(height: 32),
          Text(
            '推荐分类',
            style: Theme.of(context).textTheme.titleMedium,
          ),
          const SizedBox(height: 16),
          GridView.builder(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              childAspectRatio: 2.5,
              crossAxisSpacing: 12,
              mainAxisSpacing: 12,
            ),
            itemCount: categories.length,
            itemBuilder: (context, index) {
              final cat = categories[index];
              return Card(
                child: InkWell(
                  onTap: () {
                    _searchController.text = cat['name'] as String;
                    setState(() => _searchKeyword = cat['name'] as String);
                    _search();
                  },
                  borderRadius: BorderRadius.circular(12),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    child: Row(
                      children: [
                        Icon(
                          cat['icon'] as IconData,
                          color: AppColors.primary,
                        ),
                        const SizedBox(width: 8),
                        Text(cat['name'] as String),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

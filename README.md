# InifiniteRecyclerView
This is a tiny library for implementing endless loading list easily in Android applications, similar to those seen in Facebook or Twitter apps. It consists of InfiniteRecyclerView and AbstractInfiniteAdapter that has to be extended by user for usage in their project.

![Demo](http://i.giphy.com/l3q2WWM6bhe7kwbmw.gif)

# Usage
The library distribution is hosted on jcenter repository. To include this library in your project, simply add the following line to your module gradle file:
```
compile 'com.tenclouds:infinite-recyclerview:0.0.2'
```
You will need to implement AbstractInfiniteAdapter, ItemsLoader interface, and use them with InfiniteRecyclerView.

# ItemsLoader interface
ItemsLoader interface consists of one method - getNewItems(). This method is always called in barckground thread and provides new items to add to the list whenever the user scrolls to the bottom of the recyclerview.

# AbstractInfiniteAdapter
AbstractInfiniteAdapter has to be extended, much like "normal" recyclerview adapter, and provided with your implementation of ItemsLoader. You can use multiple view types if you want to, just like in "normal" adapter.

# InfiniteRecyclerView
This class simply needs to be used instead of normal recycler view, to send callbacks to the adapter when the view is scrolled to the bottom. It supports all of the parameters available for RecyclerView.

# License
```
Copyright 2017 Jakub Komorowski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this library except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

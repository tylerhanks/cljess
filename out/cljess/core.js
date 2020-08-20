// Compiled by ClojureScript 1.10.597 {}
goog.provide('cljess.core');
goog.require('cljs.core');
goog.require('reagent.core');
goog.require('reagent.dom');
cljess.core.piece_image = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"bb","bb",75940837),new cljs.core.Keyword(null,"wn","wn",-1618920854),new cljs.core.Keyword(null,"bn","bn",-1320495310),new cljs.core.Keyword(null,"wr","wr",576154263),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"br","br",934104792),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wk","wk",348077371),new cljs.core.Keyword(null,"wq","wq",641319707),new cljs.core.Keyword(null,"bq","bq",-1795593092),new cljs.core.Keyword(null,"wb","wb",-792891747),new cljs.core.Keyword(null,"bk","bk",1387674301)],["https://upload.wikimedia.org/wikipedia/commons/9/98/Chess_bdt45.svg","https://upload.wikimedia.org/wikipedia/commons/7/70/Chess_nlt45.svg","https://upload.wikimedia.org/wikipedia/commons/e/ef/Chess_ndt45.svg","https://upload.wikimedia.org/wikipedia/commons/7/72/Chess_rlt45.svg","https://upload.wikimedia.org/wikipedia/commons/c/c7/Chess_pdt45.svg","https://upload.wikimedia.org/wikipedia/commons/f/ff/Chess_rdt45.svg","https://upload.wikimedia.org/wikipedia/commons/4/45/Chess_plt45.svg","https://upload.wikimedia.org/wikipedia/commons/4/42/Chess_klt45.svg","https://upload.wikimedia.org/wikipedia/commons/1/15/Chess_qlt45.svg","https://upload.wikimedia.org/wikipedia/commons/4/47/Chess_qdt45.svg","https://upload.wikimedia.org/wikipedia/commons/b/b1/Chess_blt45.svg","https://upload.wikimedia.org/wikipedia/commons/f/f0/Chess_kdt45.svg"]);
cljess.core.piece_color = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"bb","bb",75940837),new cljs.core.Keyword(null,"wn","wn",-1618920854),new cljs.core.Keyword(null,"bn","bn",-1320495310),new cljs.core.Keyword(null,"wr","wr",576154263),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"br","br",934104792),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wk","wk",348077371),new cljs.core.Keyword(null,"wq","wq",641319707),new cljs.core.Keyword(null,"bq","bq",-1795593092),new cljs.core.Keyword(null,"wb","wb",-792891747),new cljs.core.Keyword(null,"bk","bk",1387674301)],[(0),(1),(0),(1),(0),(0),(1),(1),(1),(0),(1),(0)]);
cljess.core.board_to_coord = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"g6","g6",-472937504),new cljs.core.Keyword(null,"d1","d1",-1264719807),new cljs.core.Keyword(null,"a2","a2",424956801),new cljs.core.Keyword(null,"e1","e1",1921574498),new cljs.core.Keyword(null,"b2","b2",1108940514),new cljs.core.Keyword(null,"d2","d2",2138401859),new cljs.core.Keyword(null,"f8","f8",-2141475484),new cljs.core.Keyword(null,"f1","f1",1714532389),new cljs.core.Keyword(null,"h8","h8",-1986752923),new cljs.core.Keyword(null,"d6","d6",-1930141083),new cljs.core.Keyword(null,"c3","c3",-1171815738),new cljs.core.Keyword(null,"d5","d5",206360391),new cljs.core.Keyword(null,"h7","h7",1907790024),new cljs.core.Keyword(null,"e2","e2",-352276184),new cljs.core.Keyword(null,"c6","c6",700709224),new cljs.core.Keyword(null,"e3","e3",-660371736),new cljs.core.Keyword(null,"a3","a3",-290563735),new cljs.core.Keyword(null,"a1","a1",553780937),new cljs.core.Keyword(null,"e8","e8",1641281034),new cljs.core.Keyword(null,"b1","b1",-1270036758),new cljs.core.Keyword(null,"c4","c4",1028045610),new cljs.core.Keyword(null,"g4","g4",456448363),new cljs.core.Keyword(null,"g2","g2",-479351381),new cljs.core.Keyword(null,"f5","f5",1587057387),new cljs.core.Keyword(null,"e6","e6",810914252),new cljs.core.Keyword(null,"b8","b8",59364940),new cljs.core.Keyword(null,"d4","d4",1699997324),new cljs.core.Keyword(null,"c2","c2",-1561880371),new cljs.core.Keyword(null,"a8","a8",-1250124946),new cljs.core.Keyword(null,"h5","h5",-1829156625),new cljs.core.Keyword(null,"d8","d8",-2027582448),new cljs.core.Keyword(null,"a6","a6",-319298960),new cljs.core.Keyword(null,"g3","g3",-1429590160),new cljs.core.Keyword(null,"g8","g8",-450684944),new cljs.core.Keyword(null,"h4","h4",2004862993),new cljs.core.Keyword(null,"g1","g1",1986774193),new cljs.core.Keyword(null,"e4","e4",1940177521),new cljs.core.Keyword(null,"c1","c1",1132530803),new cljs.core.Keyword(null,"c8","c8",1653502291),new cljs.core.Keyword(null,"f3","f3",1954829043),new cljs.core.Keyword(null,"b7","b7",-1868108045),new cljs.core.Keyword(null,"h6","h6",557293780),new cljs.core.Keyword(null,"b4","b4",-1728006924),new cljs.core.Keyword(null,"f2","f2",396168596),new cljs.core.Keyword(null,"e5","e5",755274164),new cljs.core.Keyword(null,"a5","a5",535530230),new cljs.core.Keyword(null,"b3","b3",1128981270),new cljs.core.Keyword(null,"c5","c5",-615073545),new cljs.core.Keyword(null,"d7","d7",371666263),new cljs.core.Keyword(null,"b6","b6",1762223416),new cljs.core.Keyword(null,"g7","g7",1174072664),new cljs.core.Keyword(null,"h2","h2",-372662728),new cljs.core.Keyword(null,"f7","f7",356150168),new cljs.core.Keyword(null,"h1","h1",-1896887462),new cljs.core.Keyword(null,"d3","d3",-1338309509),new cljs.core.Keyword(null,"h3","h3",2067611163),new cljs.core.Keyword(null,"a7","a7",1513050971),new cljs.core.Keyword(null,"f6","f6",2103080604),new cljs.core.Keyword(null,"c7","c7",422231804),new cljs.core.Keyword(null,"f4","f4",990968764),new cljs.core.Keyword(null,"b5","b5",-1961609154),new cljs.core.Keyword(null,"g5","g5",310637822),new cljs.core.Keyword(null,"e7","e7",1536097022),new cljs.core.Keyword(null,"a4","a4",-1964544801)],[new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(7),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(3)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(5),(7)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(0)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(2),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(2)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(5)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(1)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(3),(6)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1),(4)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(4),(0)], null)]);
cljess.core.starting_position = new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792),new cljs.core.Keyword(null,"bn","bn",-1320495310),new cljs.core.Keyword(null,"bb","bb",75940837),new cljs.core.Keyword(null,"bq","bq",-1795593092),new cljs.core.Keyword(null,"bk","bk",1387674301),new cljs.core.Keyword(null,"bb","bb",75940837),new cljs.core.Keyword(null,"bn","bn",-1320495310),new cljs.core.Keyword(null,"br","br",934104792)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392),new cljs.core.Keyword(null,"bp","bp",-1849376392)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0),(0),(0),(0),(0),(0),(0)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0),(0),(0),(0),(0),(0),(0)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0),(0),(0),(0),(0),(0),(0)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),(0),(0),(0),(0),(0),(0),(0)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778),new cljs.core.Keyword(null,"wp","wp",1938798778)], null),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"wr","wr",576154263),new cljs.core.Keyword(null,"wn","wn",-1618920854),new cljs.core.Keyword(null,"wb","wb",-792891747),new cljs.core.Keyword(null,"wq","wq",641319707),new cljs.core.Keyword(null,"wk","wk",348077371),new cljs.core.Keyword(null,"wb","wb",-792891747),new cljs.core.Keyword(null,"wn","wn",-1618920854),new cljs.core.Keyword(null,"wr","wr",576154263)], null)], null);
if((typeof cljess !== 'undefined') && (typeof cljess.core !== 'undefined') && (typeof cljess.core.game_state !== 'undefined')){
} else {
cljess.core.game_state = reagent.core.atom.call(null,null);
}
cljess.core.new_game = (function cljess$core$new_game(){
return cljs.core.reset_BANG_.call(null,cljess.core.game_state,cljess.core.starting_position);
});
if((typeof cljess !== 'undefined') && (typeof cljess.core !== 'undefined') && (typeof cljess.core.square_selection !== 'undefined')){
} else {
cljess.core.square_selection = reagent.core.atom.call(null,null);
}
cljess.core.get_piece = (function cljess$core$get_piece(pos){
if(cljs.core.vector_QMARK_.call(null,pos)){
return cljs.core.get_in.call(null,cljs.core.deref.call(null,cljess.core.game_state),pos);
} else {
if((pos instanceof cljs.core.Keyword)){
var p = cljess.core.board_to_coord.call(null,pos);
return cljs.core.get_in.call(null,cljs.core.deref.call(null,cljess.core.game_state),p);
} else {
return null;
}
}
});
cljess.core.move_piece = (function cljess$core$move_piece(from,to){
var piece = cljess.core.get_piece.call(null,from);
cljs.core.swap_BANG_.call(null,cljess.core.game_state,(function (state,pos,piece__$1){
return cljs.core.assoc_in.call(null,state,pos,piece__$1);
}),(((from instanceof cljs.core.Keyword))?cljess.core.board_to_coord.call(null,from):((cljs.core.vector_QMARK_.call(null,from))?from:null)),(0));

return cljs.core.swap_BANG_.call(null,cljess.core.game_state,(function (state,pos,piece__$1){
return cljs.core.assoc_in.call(null,state,pos,piece__$1);
}),(((to instanceof cljs.core.Keyword))?cljess.core.board_to_coord.call(null,to):((cljs.core.vector_QMARK_.call(null,to))?to:null)),piece);
});
if((typeof cljess !== 'undefined') && (typeof cljess.core !== 'undefined') && (typeof cljess.core.legal_moves !== 'undefined')){
} else {
cljess.core.legal_moves = (function (){var method_table__4672__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4673__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4674__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4675__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4676__auto__ = cljs.core.get.call(null,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),cljs.core.get_global_hierarchy.call(null));
return (new cljs.core.MultiFn(cljs.core.symbol.call(null,"cljess.core","legal-moves"),(function (pos,board){
return cljess.core.get_piece.call(null,pos);
}),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4676__auto__,method_table__4672__auto__,prefer_table__4673__auto__,method_cache__4674__auto__,cached_hierarchy__4675__auto__));
})();
}
cljs.core._add_method.call(null,cljess.core.legal_moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792),new cljs.core.Keyword(null,"wr","wr",576154263)], null),(function (pos,board){
var vec__1147 = pos;
var x = cljs.core.nth.call(null,vec__1147,(0),null);
var y = cljs.core.nth.call(null,vec__1147,(1),null);
var moves = cljs.core.PersistentHashSet.EMPTY;
var color = cljess.core.piece_color.call(null,cljess.core.get_piece.call(null,pos));
var blocked_QMARK_ = cljs.core.atom.call(null,false);
var iter__4582__auto__ = (function cljess$core$iter__1150(s__1151){
return (new cljs.core.LazySeq(null,(function (){
var s__1151__$1 = s__1151;
while(true){
var temp__5735__auto__ = cljs.core.seq.call(null,s__1151__$1);
if(temp__5735__auto__){
var s__1151__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__1151__$2)){
var c__4580__auto__ = cljs.core.chunk_first.call(null,s__1151__$2);
var size__4581__auto__ = cljs.core.count.call(null,c__4580__auto__);
var b__1153 = cljs.core.chunk_buffer.call(null,size__4581__auto__);
if((function (){var i__1152 = (0);
while(true){
if((i__1152 < size__4581__auto__)){
var i = cljs.core._nth.call(null,c__4580__auto__,i__1152);
var piece = cljess.core.get_piece.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null));
if(cljs.core.not.call(null,cljs.core.deref.call(null,blocked_QMARK_))){
cljs.core.chunk_append.call(null,b__1153,(((piece === (0)))?cljs.core.conj.call(null,moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null)):((cljs.core._EQ_.call(null,color,cljess.core.piece_color.call(null,piece)))?cljs.core.reset_BANG_.call(null,blocked_QMARK_,true):cljs.core.reset_BANG_.call(null,blocked_QMARK_,true).call(null,cljs.core.conj.call(null,moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null))))));

var G__1154 = (i__1152 + (1));
i__1152 = G__1154;
continue;
} else {
return null;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__1153),cljess$core$iter__1150.call(null,cljs.core.chunk_rest.call(null,s__1151__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__1153),null);
}
} else {
var i = cljs.core.first.call(null,s__1151__$2);
var piece = cljess.core.get_piece.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null));
if(cljs.core.not.call(null,cljs.core.deref.call(null,blocked_QMARK_))){
return cljs.core.cons.call(null,(((piece === (0)))?cljs.core.conj.call(null,moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null)):((cljs.core._EQ_.call(null,color,cljess.core.piece_color.call(null,piece)))?cljs.core.reset_BANG_.call(null,blocked_QMARK_,true):cljs.core.reset_BANG_.call(null,blocked_QMARK_,true).call(null,cljs.core.conj.call(null,moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,y], null))))),cljess$core$iter__1150.call(null,cljs.core.rest.call(null,s__1151__$2)));
} else {
return null;
}
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4582__auto__.call(null,cljs.core.range.call(null,x,(8)));
}));
cljs.core._add_method.call(null,cljess.core.legal_moves,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bk","bk",1387674301),new cljs.core.Keyword(null,"wk","wk",348077371)], null),(function (pos,board){
var piece = cljess.core.get_piece.call(null,pos);
var moves = cljs.core.PersistentHashSet.EMPTY;
var vec__1155 = pos;
var x = cljs.core.nth.call(null,vec__1155,(0),null);
var y = cljs.core.nth.call(null,vec__1155,(1),null);
return null;
}));
cljess.core.square = (function cljess$core$square(piece,coord,color){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button","button",1456579943),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(color),"-square"].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return (((((cljs.core.deref.call(null,cljess.core.square_selection) == null)) && ((cljess.core.get_piece.call(null,coord) === (0)))))?null:(((cljs.core.deref.call(null,cljess.core.square_selection) == null))?cljs.core.reset_BANG_.call(null,cljess.core.square_selection,coord):cljess.core.move_piece.call(null,cljs.core.deref.call(null,cljess.core.square_selection),coord).call(null,cljs.core.reset_BANG_.call(null,cljess.core.square_selection,null)))).call(null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),piece], null)], null)], null);
});
cljess.core.board = (function cljess$core$board(){
var v = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["dark","light"], null);
return cljs.core.into.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632)], null),(function (){var iter__4582__auto__ = (function cljess$core$board_$_iter__1158(s__1159){
return (new cljs.core.LazySeq(null,(function (){
var s__1159__$1 = s__1159;
while(true){
var temp__5735__auto__ = cljs.core.seq.call(null,s__1159__$1);
if(temp__5735__auto__){
var s__1159__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,s__1159__$2)){
var c__4580__auto__ = cljs.core.chunk_first.call(null,s__1159__$2);
var size__4581__auto__ = cljs.core.count.call(null,c__4580__auto__);
var b__1161 = cljs.core.chunk_buffer.call(null,size__4581__auto__);
if((function (){var i__1160 = (0);
while(true){
if((i__1160 < size__4581__auto__)){
var i = cljs.core._nth.call(null,c__4580__auto__,i__1160);
cljs.core.chunk_append.call(null,b__1161,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),"board-row"], null),cljs.core.map_indexed.call(null,((function (i__1160,i,c__4580__auto__,size__4581__auto__,b__1161,s__1159__$2,temp__5735__auto__,v){
return (function (j,el){
return cljess.core.square.call(null,cljess.core.piece_image.call(null,el),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,j], null),v.call(null,cljs.core.mod.call(null,(i + j),(2))));
});})(i__1160,i,c__4580__auto__,size__4581__auto__,b__1161,s__1159__$2,temp__5735__auto__,v))
,cljs.core.nth.call(null,cljs.core.deref.call(null,cljess.core.game_state),i))], null));

var G__1162 = (i__1160 + (1));
i__1160 = G__1162;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__1161),cljess$core$board_$_iter__1158.call(null,cljs.core.chunk_rest.call(null,s__1159__$2)));
} else {
return cljs.core.chunk_cons.call(null,cljs.core.chunk.call(null,b__1161),null);
}
} else {
var i = cljs.core.first.call(null,s__1159__$2);
return cljs.core.cons.call(null,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),"board-row"], null),cljs.core.map_indexed.call(null,((function (i,s__1159__$2,temp__5735__auto__,v){
return (function (j,el){
return cljess.core.square.call(null,cljess.core.piece_image.call(null,el),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [i,j], null),v.call(null,cljs.core.mod.call(null,(i + j),(2))));
});})(i,s__1159__$2,temp__5735__auto__,v))
,cljs.core.nth.call(null,cljs.core.deref.call(null,cljess.core.game_state),i))], null),cljess$core$board_$_iter__1158.call(null,cljs.core.rest.call(null,s__1159__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4582__auto__.call(null,cljs.core.range.call(null,(8)));
})());
});
cljess.core.app = (function cljess$core$app(){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h1","h1",-1896887462),"Cljess!"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljess.core.board], null)], null);
});
cljess.core.init_BANG_ = (function cljess$core$init_BANG_(){
return reagent.core.render.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljess.core.app], null),document.getElementById("app"));
});
cljess.core.init_BANG_.call(null);

//# sourceMappingURL=core.js.map

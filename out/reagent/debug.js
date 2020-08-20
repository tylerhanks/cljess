// Compiled by ClojureScript 1.10.597 {}
goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = (typeof console !== 'undefined');
reagent.debug.tracking = false;
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.warnings !== 'undefined')){
} else {
reagent.debug.warnings = cljs.core.atom.call(null,null);
}
if((typeof reagent !== 'undefined') && (typeof reagent.debug !== 'undefined') && (typeof reagent.debug.track_console !== 'undefined')){
} else {
reagent.debug.track_console = (function (){var o = ({});
(o.warn = (function() { 
var G__667__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__667 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__668__i = 0, G__668__a = new Array(arguments.length -  0);
while (G__668__i < G__668__a.length) {G__668__a[G__668__i] = arguments[G__668__i + 0]; ++G__668__i;}
  args = new cljs.core.IndexedSeq(G__668__a,0,null);
} 
return G__667__delegate.call(this,args);};
G__667.cljs$lang$maxFixedArity = 0;
G__667.cljs$lang$applyTo = (function (arglist__669){
var args = cljs.core.seq(arglist__669);
return G__667__delegate(args);
});
G__667.cljs$core$IFn$_invoke$arity$variadic = G__667__delegate;
return G__667;
})()
);

(o.error = (function() { 
var G__670__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__670 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__671__i = 0, G__671__a = new Array(arguments.length -  0);
while (G__671__i < G__671__a.length) {G__671__a[G__671__i] = arguments[G__671__i + 0]; ++G__671__i;}
  args = new cljs.core.IndexedSeq(G__671__a,0,null);
} 
return G__670__delegate.call(this,args);};
G__670.cljs$lang$maxFixedArity = 0;
G__670.cljs$lang$applyTo = (function (arglist__672){
var args = cljs.core.seq(arglist__672);
return G__670__delegate(args);
});
G__670.cljs$core$IFn$_invoke$arity$variadic = G__670__delegate;
return G__670;
})()
);

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
(reagent.debug.tracking = true);

cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

f.call(null);

var warns = cljs.core.deref.call(null,reagent.debug.warnings);
cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

(reagent.debug.tracking = false);

return warns;
});

//# sourceMappingURL=debug.js.map

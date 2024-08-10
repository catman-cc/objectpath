// Generated from /Users/jpanda/work/codes/catman/catman-jsonpath/catman-jsonpath/src/main/resources/ObjectPath.g4 by ANTLR 4.13.1
package cc.catman.object.path.standard;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ObjectPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ObjectPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#objectPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPath(ObjectPathParser.ObjectPathContext ctx);
	/**
	 * Visit a parse tree produced by the {@code COMPLEX_VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCOMPLEX_VALUE_EXPR(ObjectPathParser.COMPLEX_VALUE_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EXPR_FUNC_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEXPR_FUNC_EXPR(ObjectPathParser.EXPR_FUNC_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GROUP_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGROUP_EXPR(ObjectPathParser.GROUP_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CALCULATE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCALCULATE_EXPR(ObjectPathParser.CALCULATE_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOR(ObjectPathParser.ORContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRREGEX(ObjectPathParser.RREGEXContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLT(ObjectPathParser.LTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DEFAULT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDEFAULT_EXPR(ObjectPathParser.DEFAULT_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEQ(ObjectPathParser.EQContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGT(ObjectPathParser.GTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLREGEX(ObjectPathParser.LREGEXContext ctx);
	/**
	 * Visit a parse tree produced by the {@code REG_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitREG_EXPR(ObjectPathParser.REG_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVALUE_EXPR(ObjectPathParser.VALUE_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LIKE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLIKE(ObjectPathParser.LIKEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AND}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAND(ObjectPathParser.ANDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TYPE_CAST_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTYPE_CAST_EXPR(ObjectPathParser.TYPE_CAST_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLE(ObjectPathParser.LEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ISNULL}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitISNULL(ObjectPathParser.ISNULLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNEQ(ObjectPathParser.NEQContext ctx);
	/**
	 * Visit a parse tree produced by the {@code REVERT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitREVERT_EXPR(ObjectPathParser.REVERT_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PATH_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPATH_EXPR(ObjectPathParser.PATH_EXPRContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGE(ObjectPathParser.GEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ROOT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitROOT_NODE(ObjectPathParser.ROOT_NODEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CURRENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCURRENT_NODE(ObjectPathParser.CURRENT_NODEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PARENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPARENT_NODE(ObjectPathParser.PARENT_NODEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code METHOD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMETHOD(ObjectPathParser.METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCHILD(ObjectPathParser.CHILDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RECURSIVE_CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRECURSIVE_CHILD(ObjectPathParser.RECURSIVE_CHILDContext ctx);
	/**
	 * Visit a parse tree produced by the {@code INDEX_OR_NAME}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitINDEX_OR_NAME(ObjectPathParser.INDEX_OR_NAMEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code INDEX_OR_NAME_LIST}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitINDEX_OR_NAME_LIST(ObjectPathParser.INDEX_OR_NAME_LISTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code INDEX}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitINDEX(ObjectPathParser.INDEXContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WILDCARD_ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWILDCARD_ALL(ObjectPathParser.WILDCARD_ALLContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SLICE}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSLICE(ObjectPathParser.SLICEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SLICE_PICK}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSLICE_PICK(ObjectPathParser.SLICE_PICKContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FILTER}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFILTER(ObjectPathParser.FILTERContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SCRIPT}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSCRIPT(ObjectPathParser.SCRIPTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UP}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUP(ObjectPathParser.UPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitALL(ObjectPathParser.ALLContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#pipe}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPipe(ObjectPathParser.PipeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#typeCast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCast(ObjectPathParser.TypeCastContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#filterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterExpr(ObjectPathParser.FilterExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMIN(ObjectPathParser.MINContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MAX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMAX(ObjectPathParser.MAXContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SUM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSUM(ObjectPathParser.SUMContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AVG}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAVG(ObjectPathParser.AVGContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SIZE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSIZE(ObjectPathParser.SIZEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ISEMPTY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitISEMPTY(ObjectPathParser.ISEMPTYContext ctx);
	/**
	 * Visit a parse tree produced by the {@code METHOD_INDEX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMETHOD_INDEX(ObjectPathParser.METHOD_INDEXContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LENGTH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLENGTH(ObjectPathParser.LENGTHContext ctx);
	/**
	 * Visit a parse tree produced by the {@code KEYS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKEYS(ObjectPathParser.KEYSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VALUES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVALUES(ObjectPathParser.VALUESContext ctx);
	/**
	 * Visit a parse tree produced by the {@code REVERSE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitREVERSE(ObjectPathParser.REVERSEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DISTINCT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDISTINCT(ObjectPathParser.DISTINCTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSORT(ObjectPathParser.SORTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RSORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRSORT(ObjectPathParser.RSORTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GROUPBY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGROUPBY(ObjectPathParser.GROUPBYContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JOIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJOIN(ObjectPathParser.JOINContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SPLIT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSPLIT(ObjectPathParser.SPLITContext ctx);
	/**
	 * Visit a parse tree produced by the {@code REPLACE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitREPLACE(ObjectPathParser.REPLACEContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SUBSTRING}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSUBSTRING(ObjectPathParser.SUBSTRINGContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TOUPPER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTOUPPER(ObjectPathParser.TOUPPERContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TOLOWER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTOLOWER(ObjectPathParser.TOLOWERContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTRIM(ObjectPathParser.TRIMContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLTRIM(ObjectPathParser.LTRIMContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRTRIM(ObjectPathParser.RTRIMContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CONCAT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCONCAT(ObjectPathParser.CONCATContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FIRST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFIRST(ObjectPathParser.FIRSTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LAST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAST(ObjectPathParser.LASTContext ctx);
	/**
	 * Visit a parse tree produced by the {@code INDEXOF}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitINDEXOF(ObjectPathParser.INDEXOFContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MAP}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMAP(ObjectPathParser.MAPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TOMAP_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTOMAP_METHOD(ObjectPathParser.TOMAP_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IS_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIS_NULL_METHOD(ObjectPathParser.IS_NULL_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NOT_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNOT_NULL_METHOD(ObjectPathParser.NOT_NULL_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AND_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAND_METHOD(ObjectPathParser.AND_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OR_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOR_METHOD(ObjectPathParser.OR_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NOT_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNOT_METHOD(ObjectPathParser.NOT_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FILTER_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFILTER_METHOD(ObjectPathParser.FILTER_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BETWEEN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBETWEEN(ObjectPathParser.BETWEENContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LIKE_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLIKE_METHOD(ObjectPathParser.LIKE_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIN(ObjectPathParser.INContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NOTIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNOTIN(ObjectPathParser.NOTINContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CONTAINS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCONTAINS(ObjectPathParser.CONTAINSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code STARTSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSTARTSWITH(ObjectPathParser.STARTSWITHContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ENDSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitENDSWITH(ObjectPathParser.ENDSWITHContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MATCHES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMATCHES(ObjectPathParser.MATCHESContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TOSTRING_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTOSTRING_METHOD(ObjectPathParser.TOSTRING_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RAW_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRAW_METHOD(ObjectPathParser.RAW_METHODContext ctx);
	/**
	 * Visit a parse tree produced by the {@code METHOD_CALL}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMETHOD_CALL(ObjectPathParser.METHOD_CALLContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#scripts}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScripts(ObjectPathParser.ScriptsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#scripts_start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScripts_start(ObjectPathParser.Scripts_startContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#scripts_end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScripts_end(ObjectPathParser.Scripts_endContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#namedArgs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgs(ObjectPathParser.NamedArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#namedArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArg(ObjectPathParser.NamedArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#args}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgs(ObjectPathParser.ArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(ObjectPathParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#complexValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexValue(ObjectPathParser.ComplexValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ObjectPathParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#json}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJson(ObjectPathParser.JsonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(ObjectPathParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(ObjectPathParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link ObjectPathParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(ObjectPathParser.PairContext ctx);
}
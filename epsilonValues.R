# Libraries
library(ggplot2)
convergence <- read.csv(file.choose(), header=FALSE, row.names=1)
men <-  rowMeans(convergence[,-1])
ba <- barplot(names=rownames(convergence), height=men, ylim=c(0,max(men) +100), ylab = "avg. episodes until convergence", xlab = "epsilon value")
text(x = ba, y = men, label = as.integer(men), pos = 3, cex = 0.8, col = "red")

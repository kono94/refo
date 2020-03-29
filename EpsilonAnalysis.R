# Libraries
library(ggplot2)
library(matrixStats)
convergence <- read.csv(file.choose(), header=FALSE, row.names=1, skip=1)

sds <- rowSds(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
men <- rowMeans(sapply(convergence[,-1], `length<-`, max(lengths(convergence[,-1]))), na.rm=TRUE)
print(sds)

# create dummy data
data <- data.frame(
  names=rownames(convergence),
  means=men,
  sds=sds
)

convergence$group <- row.names(convergence)
convergence.m <- melt(convergence, id.vars = "groups")

ggplot(data) +
  geom_bar(aes(x=names, y=means, fill=means), stat="identity", colour="black", alpha=0.8) +
  geom_errorbar( aes(x=names, ymin=means, ymax=means+sds), width=0.4, colour="black", alpha=0.8, size=0.6) +
  xlab("Epsilon") + ylab("Ø Episoden bis Konvergenz") 